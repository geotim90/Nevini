package de.nevini.bot.services.osu;

import de.nevini.api.ApiBackedCache;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.OsuApi;
import de.nevini.api.osu.model.*;
import de.nevini.api.osu.requests.OsuScoresRequest;
import de.nevini.api.osu.requests.OsuUserBestRequest;
import de.nevini.api.osu.requests.OsuUserRecentRequest;
import de.nevini.api.osu.requests.OsuUserRequest;
import de.nevini.bot.data.osu.OsuApiProvider;
import de.nevini.bot.data.osu.beatmap.OsuBeatmapDataService;
import de.nevini.bot.data.osu.user.OsuUserDataService;
import de.nevini.bot.db.game.GameData;
import de.nevini.bot.scope.Locatable;
import de.nevini.bot.services.common.GameService;
import de.nevini.commons.util.Finder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class OsuService implements Locatable {

    private final Lazy<GameData> game;

    private final OsuBeatmapDataService beatmapDataService;
    private final OsuUserDataService userDataService;
    private final ApiBackedCache<List<OsuScore>> scoresShortCache;
    private final ApiBackedCache<List<OsuUser>> userLargeCache;
    private final ApiBackedCache<List<OsuUserBest>> userBestShortCache;
    private final ApiBackedCache<List<OsuUserRecent>> userRecentShortCache;

    public OsuService(
            @Autowired OsuApiProvider apiProvider,
            @Autowired GameService gameService,
            @Autowired OsuBeatmapDataService beatmapDataService,
            @Autowired OsuUserDataService userDataService
    ) {
        OsuApi api = apiProvider.getApi();
        game = Lazy.of(() -> gameService.findGames("osu!").stream().findFirst().orElse(null));

        this.beatmapDataService = beatmapDataService;
        this.userDataService = userDataService;
        scoresShortCache = new ApiBackedCache<>(api, Duration.ofMinutes(5), 100);
        userLargeCache = new ApiBackedCache<>(api, Duration.ofDays(1), 10000);
        userBestShortCache = new ApiBackedCache<>(api, Duration.ofMinutes(5), 100);
        userRecentShortCache = new ApiBackedCache<>(api, Duration.ofMinutes(5), 100);
    }

    public GameData getGame() {
        return game.getOptional().orElseThrow(() -> new IllegalStateException("No game data!"));
    }

    public Collection<OsuBeatmap> findBeatmaps(@NonNull String query) {
        try {
            OsuBeatmap beatmap = getBeatmap(Integer.parseInt(query));
            if (beatmap != null) {
                return Collections.singleton(beatmap);
            }
        } catch (NumberFormatException ignore) {
        }
        return Finder.find(beatmapDataService.findAllByTitleContainsIgnoreCase(query), OsuBeatmap::getTitle, query);
    }

    public OsuBeatmap getBeatmap(int beatmapId) {
        return beatmapDataService.get(beatmapId);
    }

    public String getBeatmapString(int beatmapId) {
        OsuBeatmap beatmap = beatmapDataService.getCached(beatmapId);
        return beatmap == null
                ? "https://osu.ppy.sh/b/" + beatmapId
                : beatmap.getArtist() + " - " + beatmap.getTitle()
                + " [" + beatmap.getVersion() + "] (" + beatmap.getMode().getName() + ")";
    }

    public List<OsuScore> getScores(int beatmap, String ign, OsuMode mode, OsuMod[] mods) {
        OsuScoresRequest.OsuScoresRequestBuilder requestBuilder = OsuScoresRequest.builder()
                .beatmapId(beatmap)
                .limit(100);
        if (StringUtils.isNotEmpty(ign)) {
            requestBuilder.user(ign);
            requestBuilder.userType(OsuUserType.STRING);
        }
        if (mode != null) {
            requestBuilder.mode(mode);
        }
        if (mods != null) {
            requestBuilder.mods(mods);
        }
        log.debug("Requesting scores (beatmap={}, ign={}, mode={}, mods={})", beatmap, ign, mode, mods);
        ApiResponse<List<OsuScore>> response = scoresShortCache.getOrCall(requestBuilder.build());
        if (response.isOk()) {
            return response.getResult();
        } else if (response.isRateLimited()) {
            log.info("Failed to get scores for {} (rate limited)", beatmap);
            return null;
        } else {
            log.info("Failed to get scores for {}", beatmap, response.getThrowable());
            return null;
        }
    }

    public OsuUser getUser(@NonNull String user) {
        return getUser(user, null);
    }

    public OsuUser getUser(@NonNull String user, OsuMode mode) {
        return userDataService.get(user, mode);
    }

    public List<OsuUserBest> getUserBest(@NonNull String user, OsuMode mode) {
        log.debug("Requesting userBest (user={}, mode={})", user, mode);
        ApiResponse<List<OsuUserBest>> response = userBestShortCache.getOrCall(OsuUserBestRequest.builder()
                .user(user)
                .userType(OsuUserType.STRING)
                .mode(ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD))
                .limit(100)
                .build());
        if (response.isOk()) {
            return response.getResult();
        } else if (response.isRateLimited()) {
            log.info("Failed to get user best for {} (rate limited)", user);
            return null;
        } else {
            log.info("Failed to get user best for {}", user, response.getThrowable());
            return null;
        }
    }

    public OsuUser getUserEvents(@NonNull String user, OsuMode mode, int eventDays) {
        log.debug("Requesting user (user={}, mode={}, eventDays={})", user, mode, eventDays);
        ApiResponse<List<OsuUser>> response = userLargeCache.callOrGet(OsuUserRequest.builder()
                .user(user)
                .userType(OsuUserType.STRING)
                .mode(ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD))
                .eventDays(eventDays)
                .build());
        if (response.isOk()) {
            List<OsuUser> users = response.getResult();
            if (users == null || users.isEmpty()) {
                return null;
            } else {
                return users.get(0);
            }
        } else if (response.isRateLimited()) {
            log.info("Failed to get user {} (rate limited)", user);
            return null;
        } else {
            log.info("Failed to get user {}", user, response.getThrowable());
            return null;
        }
    }

    public String getUserName(int userId) {
        OsuUser user = userDataService.getCached(userId);
        return user == null ? Integer.toString(userId) : user.getUserName();
    }

    public List<OsuUserRecent> getUserRecent(@NonNull String user, OsuMode mode) {
        log.debug("Requesting userRecent (user={}, mode={})", user, mode);
        ApiResponse<List<OsuUserRecent>> response = userRecentShortCache.getOrCall(OsuUserRecentRequest.builder()
                .user(user)
                .userType(OsuUserType.STRING)
                .mode(ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD))
                .limit(50)
                .build());
        if (response.isOk()) {
            return response.getResult();
        } else if (response.isRateLimited()) {
            log.info("Failed to get user recent for {} (rate limited)", user);
            return null;
        } else {
            log.info("Failed to get user recent for {}", user, response.getThrowable());
            return null;
        }
    }

}
