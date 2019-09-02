package de.nevini.services.osu;

import de.nevini.api.osu.external.requests.*;
import de.nevini.api.osu.model.*;
import de.nevini.api.osu.services.*;
import de.nevini.jpa.game.GameData;
import de.nevini.locators.Locatable;
import de.nevini.services.common.GameService;
import de.nevini.util.Formatter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OsuService implements Locatable {

    private final Lazy<GameData> game;

    private final OsuBeatmapSearchService beatmapSearchService;
    private final OsuBeatmapService beatmapService;
    private final OsuScoreService scoresService;
    private final OsuUserService userService;
    private final OsuUserBestService userBestService;
    private final OsuUserEventService userEventService;
    private final OsuUserRecentService userRecentService;

    public OsuService(
            @Autowired GameService gameService,
            @Autowired OsuBeatmapSearchService beatmapSearchService,
            @Autowired OsuBeatmapService beatmapService,
            @Autowired OsuScoreService scoresService,
            @Autowired OsuUserService userService,
            @Autowired OsuUserBestService userBestService,
            @Autowired OsuUserEventService userEventService,
            @Autowired OsuUserRecentService userRecentService
    ) {
        game = Lazy.of(() -> gameService.findGames("osu!").stream().findFirst().orElse(null));
        this.beatmapSearchService = beatmapSearchService;
        this.beatmapService = beatmapService;
        this.scoresService = scoresService;
        this.userService = userService;
        this.userBestService = userBestService;
        this.userEventService = userEventService;
        this.userRecentService = userRecentService;
    }

    public GameData getGame() {
        return game.getOptional().orElse(new GameData(367827983903490050L, "osu!", null));
    }

    public List<OsuBeatmap> findBeatmaps(@NonNull String query) {
        try {
            int id = Integer.parseInt(query);
            OsuBeatmap beatmap = getBeatmap(id);
            if (beatmap != null) {
                return Collections.singletonList(beatmap);
            }
            List<OsuBeatmap> beatmapset = beatmapService.get(
                    OsuApiGetBeatmapsRequest.builder().beatmapsetId(id).build()).getResult();
            if (beatmapset != null && !beatmapset.isEmpty()) {
                return beatmapset.stream().sorted(Comparator.comparing(OsuBeatmap::getMode)
                        .thenComparing(OsuBeatmap::getDifficultyRating)).collect(Collectors.toList());
            }
        } catch (NumberFormatException ignore) {
        }
        return beatmapSearchService.search(query);
    }

    public OsuBeatmap getBeatmap(int beatmapId) {
        return beatmapService.get(beatmapId).getResult();
    }

    public String getBeatmapString(int beatmapId) {
        OsuBeatmap beatmap = beatmapService.getCached(beatmapId).getResult();
        return beatmap == null ? "https://osu.ppy.sh/b/" + beatmapId : Formatter.formatOsuBeatmap(beatmap);
    }

    public List<OsuScore> getScores(int beatmap, String ign, OsuMode mode, OsuMod[] mods) {
        OsuApiGetScoresRequest.OsuApiGetScoresRequestBuilder requestBuilder = OsuApiGetScoresRequest.builder()
                .beatmapId(beatmap)
                .limit(100);
        if (StringUtils.isNotEmpty(ign)) {
            requestBuilder.user(ign);
            requestBuilder.userType(OsuUserType.STRING);
        }
        if (mode != null) {
            requestBuilder.mode(mode.getId());
        }
        if (mods != null) {
            requestBuilder.mods(OsuMod.sum(mods));
        }
        return scoresService.get(requestBuilder.build()).getResult();
    }

    public OsuUser getUser(@NonNull String user) {
        return getUser(user, null);
    }

    public OsuUser getUser(@NonNull String user, OsuMode mode) {
        List<OsuUser> result = userService.get(OsuApiGetUserRequest.builder()
                .user(user)
                .userType(OsuUserType.STRING)
                .mode(ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD).getId())
                .build()).getResult();
        return result == null || result.isEmpty() ? null : result.get(0);
    }

    public List<OsuUserBest> getUserBest(@NonNull String user, OsuMode mode) {
        return userBestService.get(OsuApiGetUserBestRequest.builder()
                .user(user)
                .userType(OsuUserType.STRING)
                .mode(ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD).getId())
                .limit(100)
                .build()).getResult();
    }

    public List<OsuUserEvent> getUserEvents(@NonNull String user, OsuMode mode, int eventDays) {
        return userEventService.get(OsuApiGetUserRequest.builder()
                .user(user)
                .userType(OsuUserType.STRING)
                .mode(ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD).getId())
                .eventDays(eventDays)
                .build()).getResult();
    }

    public String getUserName(int userId) {
        OsuUser user = userService.getCached(userId, OsuMode.STANDARD.getId()).getResult();
        return user == null ? Integer.toString(userId) : user.getUserName();
    }

    public List<OsuUserRecent> getUserRecent(@NonNull String user, OsuMode mode) {
        return userRecentService.get(OsuApiGetUserRecentRequest.builder()
                .user(user)
                .userType(OsuUserType.STRING)
                .mode(ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD).getId())
                .limit(50)
                .build()).getResult();
    }

}
