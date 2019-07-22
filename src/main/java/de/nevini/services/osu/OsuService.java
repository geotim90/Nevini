package de.nevini.services.osu;

import de.nevini.api.osu.model.*;
import de.nevini.api.osu.requests.OsuScoresRequest;
import de.nevini.api.osu.requests.OsuUserBestRequest;
import de.nevini.api.osu.requests.OsuUserRecentRequest;
import de.nevini.api.osu.requests.OsuUserRequest;
import de.nevini.data.osu.*;
import de.nevini.jpa.game.GameData;
import de.nevini.scope.Locatable;
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

    private final OsuBeatmapDataService beatmapDataService;
    private final OsuUserDataService userDataService;
    private final OsuScoresDataService scoresDataService;
    private final OsuUserBestDataService userBestDataService;
    private final OsuUserRecentDataService userRecentDataService;

    public OsuService(
            @Autowired GameService gameService,
            @Autowired OsuBeatmapDataService beatmapDataService,
            @Autowired OsuUserDataService userDataService,
            @Autowired OsuScoresDataService scoresDataService,
            @Autowired OsuUserBestDataService userBestDataService,
            @Autowired OsuUserRecentDataService userRecentDataService
    ) {
        game = Lazy.of(() -> gameService.findGames("osu!").stream().findFirst().orElse(null));

        this.beatmapDataService = beatmapDataService;
        this.userDataService = userDataService;
        this.scoresDataService = scoresDataService;
        this.userBestDataService = userBestDataService;
        this.userRecentDataService = userRecentDataService;
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
        return scoresDataService.get(requestBuilder.build());
    }

    public OsuUser getUser(@NonNull String user) {
        return getUser(user, null);
    }

    public OsuUser getUser(@NonNull String user, OsuMode mode) {
        return userDataService.get(user, mode);
    }

    public List<OsuUserBest> getUserBest(@NonNull String user, OsuMode mode) {
        return userBestDataService.get(OsuUserBestRequest.builder()
                .user(user)
                .userType(OsuUserType.STRING)
                .mode(ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD))
                .limit(100)
                .build());
    }

    public OsuUser getUserEvents(@NonNull String user, OsuMode mode, int eventDays) {
        return userDataService.get(OsuUserRequest.builder()
                .user(user)
                .userType(OsuUserType.STRING)
                .mode(ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD))
                .eventDays(eventDays)
                .build());
    }

    public String getUserName(int userId) {
        OsuUser user = userDataService.getCached(userId);
        return user == null ? Integer.toString(userId) : user.getUserName();
    }

    public List<OsuUserRecent> getUserRecent(@NonNull String user, OsuMode mode) {
        return userRecentDataService.get(OsuUserRecentRequest.builder()
                .user(user)
                .userType(OsuUserType.STRING)
                .mode(ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD))
                .limit(50)
                .build());
    }

}
