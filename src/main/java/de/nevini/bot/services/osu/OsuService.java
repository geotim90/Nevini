package de.nevini.bot.services.osu;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.OsuApi;
import de.nevini.api.osu.model.*;
import de.nevini.api.osu.requests.*;
import de.nevini.bot.db.game.GameData;
import de.nevini.bot.scope.Locatable;
import de.nevini.bot.services.common.GameService;
import de.nevini.commons.util.Finder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class OsuService implements Locatable {

    private final Map<Integer, OsuBeatmap> beatmapCache = new ConcurrentHashMap<>();
    private final Map<Integer, String> userNameCache = new ConcurrentHashMap<>();

    private final OsuApi osuApi;
    private final GameService gameService;

    public OsuService(
            @Value("${osu.token:#{null}}") String token,
            @Autowired GameService gameService
    ) {
        osuApi = new OsuApi(new OkHttpClient.Builder().build(), token);
        this.gameService = gameService;
    }

    /**
     * Attempts to find beatmaps by id or name.<br>
     * If a valid id is provided, this will query the <em>cached</em> beatmap information from or retrieve it from the
     * osu!api.<br>
     * If part of a title is provided, this will query the <em>cached</em> beatmaps and return the best matches.
     */
    public Collection<OsuBeatmap> findBeatmaps(@NonNull String query) {
        try {
            int beatmapId = Integer.parseInt(query);
            return Collections.singleton(getBeatmapFromCacheOrApi(beatmapId));
        } catch (NumberFormatException ignore) {
            return Finder.find(beatmapCache.values(), OsuBeatmap::getTitle, query);
        }
    }

    /**
     * Retrieves beatmap information from the osu!api.
     */
    public OsuBeatmap getBeatmap(int beatmapId) {
        OsuBeatmap beatmap = getBeatmapFromApi(beatmapId);
        if (beatmap != null) {
            beatmapCache.put(beatmap.getBeatmapId(), beatmap);
        }
        return beatmap;
    }

    private OsuBeatmap getBeatmapFromCacheOrApi(int beatmapId) {
        return beatmapCache.computeIfAbsent(beatmapId, this::getBeatmapFromApi);
    }

    private OsuBeatmap getBeatmapFromApi(int beatmapId) {
        log.info("Requesting beatmap (id={})", beatmapId);
        ApiResponse<List<OsuBeatmap>> response = osuApi.getBeatmaps(
                OsuBeatmapsRequest.builder().beatmapId(beatmapId).build()
        );
        if (response.isOk()) {
            if (response.getResult() == null || response.getResult().isEmpty()) {
                return null;
            } else {
                return response.getResult().get(0);
            }
        } else if (response.isRateLimited()) {
            log.info("Failed to get beatmap {} (rate limited)", beatmapId);
            return null;
        } else {
            log.info("Failed to get beatmap {}", beatmapId, response.getThrowable());
            return null;
        }
    }

    public String getBeatmapString(int beatmapId) {
        OsuBeatmap beatmap = getBeatmapFromCacheOrApi(beatmapId);
        return beatmap == null
                ? "https://osu.ppy.sh/b/" + beatmapId
                : beatmap.getArtist() + " - " + beatmap.getTitle()
                + " [" + beatmap.getVersion() + "] (" + beatmap.getMode().getName() + ")";
    }

    public GameData getGame() {
        return gameService.findGames("osu!").stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No game data!"));
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
        log.info("Requesting scores (beatmap={}, ign={}, mode={}, mods={})", beatmap, ign, mode, mods);
        ApiResponse<List<OsuScore>> response = osuApi.getScores(requestBuilder.build());
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

    public OsuUser getUser(int user) {
        return getUser(user, OsuMode.STANDARD, 1);
    }

    public OsuUser getUser(int user, OsuMode mode) {
        return getUser(user, mode, 1);
    }

    public OsuUser getUser(int user, OsuMode mode, int eventDays) {
        log.info("Requesting user (user={}, mode={}, eventDays={})", user, mode, eventDays);
        return getUser(OsuUserRequest.builder().user(Integer.toString(user)).userType(OsuUserType.ID), mode, eventDays);
    }

    public OsuUser getUser(@NonNull String user) {
        return getUser(user, OsuMode.STANDARD, 1);
    }

    public OsuUser getUser(@NonNull String user, OsuMode mode) {
        return getUser(user, mode, 1);
    }

    public OsuUser getUser(@NonNull String user, OsuMode mode, int eventDays) {
        log.info("Requesting users (user={}, mode={}, eventDays={})", user, mode, eventDays);
        return getUser(OsuUserRequest.builder().user(user).userType(OsuUserType.STRING), mode, eventDays);
    }

    private OsuUser getUser(OsuUserRequest.OsuUserRequestBuilder requestBuilder, OsuMode mode, int eventDays) {
        ApiResponse<List<OsuUser>> response = osuApi.getUser(requestBuilder
                .mode(ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD))
                .eventDays(eventDays)
                .build());
        if (response.isOk()) {
            List<OsuUser> users = response.getResult();
            if (users == null || users.isEmpty()) {
                return null;
            } else {
                OsuUser user = users.get(0);
                userNameCache.put(user.getUserId(), user.getUserName());
                return user;
            }
        } else if (response.isRateLimited()) {
            log.info("Failed to get user {} (rate limited)", requestBuilder);
            return null;
        } else {
            log.info("Failed to get user {}", requestBuilder, response.getThrowable());
            return null;
        }
    }

    public List<OsuUserBest> getUserBest(@NonNull String user, OsuMode mode) {
        log.info("Requesting userBest (user={}, mode={})", user, mode);
        ApiResponse<List<OsuUserBest>> response = osuApi.getUserBest(OsuUserBestRequest.builder()
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

    public String getUserName(int userID) {
        String userName = userNameCache.get(userID);
        if (StringUtils.isEmpty(userName)) {
            OsuUser user = getUser(userID);
            return user == null ? Integer.toString(userID) : user.getUserName();
        } else {
            return userName;
        }
    }

    public List<OsuUserRecent> getUserRecent(@NonNull String user, OsuMode mode) {
        log.info("Requesting userRecent (user={}, mode={})", user, mode);
        ApiResponse<List<OsuUserRecent>> response = osuApi.getUserRecent(OsuUserRecentRequest.builder()
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
