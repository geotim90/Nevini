package de.nevini.services.osu;

import de.nevini.api.osu.external.requests.*;
import de.nevini.api.osu.model.*;
import de.nevini.api.osu.services.*;
import de.nevini.jpa.game.GameData;
import de.nevini.locators.Locatable;
import de.nevini.services.common.GameService;
import de.nevini.util.Finder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class OsuService implements Locatable {

    private final Lazy<GameData> game;

    private final OsuBeatmapService beatmapService;
    private final OsuScoreService scoresService;
    private final OsuUserService userService;
    private final OsuUserBestService userBestService;
    private final OsuUserEventService userEventService;
    private final OsuUserRecentService userRecentService;

    public OsuService(
            @Autowired GameService gameService,
            @Autowired OsuBeatmapService beatmapService,
            @Autowired OsuScoreService scoresService,
            @Autowired OsuUserService userService,
            @Autowired OsuUserBestService userBestService,
            @Autowired OsuUserEventService userEventService,
            @Autowired OsuUserRecentService userRecentService
    ) {
        game = Lazy.of(() -> gameService.findGames("osu!").stream().findFirst().orElse(null));

        this.beatmapService = beatmapService;
        this.scoresService = scoresService;
        this.userService = userService;
        this.userBestService = userBestService;
        this.userEventService = userEventService;
        this.userRecentService = userRecentService;
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
        return Finder.find(beatmapService.find(query), OsuBeatmap::getTitle, query);
    }

    public OsuBeatmap getBeatmap(int beatmapId) {
        return beatmapService.get(beatmapId).getResult();
    }

    public String getBeatmapString(int beatmapId) {
        OsuBeatmap beatmap = beatmapService.getCached(beatmapId).getResult();
        return beatmap == null
                ? "https://osu.ppy.sh/b/" + beatmapId
                : beatmap.getArtist() + " - " + beatmap.getTitle()
                + " [" + beatmap.getVersion() + "] (" + beatmap.getMode().getName() + ")";
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
        return userService.get(OsuApiGetUserRequest.builder()
                .user(user)
                .userType(OsuUserType.STRING)
                .mode(ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD).getId())
                .build()).getResult().get(0);
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
