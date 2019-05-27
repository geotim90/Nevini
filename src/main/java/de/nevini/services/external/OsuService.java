package de.nevini.services.external;

import com.oopsjpeg.osu4j.*;
import com.oopsjpeg.osu4j.backend.*;
import com.oopsjpeg.osu4j.exception.OsuAPIException;
import de.nevini.db.game.GameData;
import de.nevini.services.common.GameService;
import de.nevini.util.Finder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OsuService {

    private final Map<Integer, String> beatmapNameCache = new ConcurrentHashMap<>();
    private final Map<Integer, String> userNameCache = new ConcurrentHashMap<>();

    private final Osu osu;
    private final GameService gameService;

    public OsuService(
            @Value("${osu.token:#{null}}") String token,
            @Autowired GameService gameService
    ) {
        osu = Osu.getAPI(token);
        this.gameService = gameService;
    }

    public String getBeatmapName(int beatmapId) {
        String beatmapName = beatmapNameCache.get(beatmapId);
        if (StringUtils.isEmpty(beatmapName)) {
            OsuBeatmap beatmap = getBeatmap(beatmapId);
            return beatmap == null ? Integer.toString(beatmapId) : beatmap.getTitle();
        } else {
            return beatmapName;
        }
    }

    public Map<Integer, String> findBeatmaps(@NonNull String query) {
        try {
            int id = Integer.parseInt(query);
            return Collections.singletonMap(id, getBeatmapName(id));
        } catch (NumberFormatException ignore) {
            return Finder.find(beatmapNameCache.entrySet(), Map.Entry::getValue, query).stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }

    public OsuBeatmap getBeatmap(int beatmapId) {
        OsuBeatmap beatmap;
        try {
            beatmap = osu.beatmaps.query(new EndpointBeatmaps.ArgumentsBuilder().setBeatmapID(beatmapId).build())
                    .get(0);
        } catch (OsuAPIException | RuntimeException e) {
            log.info("Failed to get beatmap {}", beatmapId, e);
            return null;
        }
        if (beatmap != null) {
            beatmapNameCache.put(beatmap.getID(), beatmap.getTitle());
        }
        return beatmap;
    }

    public GameData getGame() {
        return gameService.findGames("osu!").stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No game data!"));
    }

    public List<OsuScore> getScores(int beatmap, String ign, GameMode mode, GameMod[] mods) {
        EndpointScores.ArgumentsBuilder builder = new EndpointScores.ArgumentsBuilder(beatmap).setLimit(100);
        if (StringUtils.isNotEmpty(ign)) {
            builder.setUserName(ign);
        }
        if (mode != null) {
            builder.setMode(mode);
        }
        if (mods != null) {
            EnumSet<GameMod> modSet = EnumSet.noneOf(GameMod.class);
            modSet.addAll(Arrays.asList(mods));
            builder.setMods(modSet);
        }
        try {
            return osu.scores.query(builder.build());
        } catch (OsuAPIException | RuntimeException e) {
            log.info("Failed to get scores for {}", beatmap, e);
            return null;
        }
    }

    public OsuUser getUser(int user) {
        return getUser(user, GameMode.STANDARD, 1);
    }

    public OsuUser getUser(int user, GameMode mode) {
        return getUser(user, mode, 1);
    }

    public OsuUser getUser(int user, GameMode mode, int eventDays) {
        return getUser(new EndpointUsers.ArgumentsBuilder(user), mode, eventDays);
    }

    public OsuUser getUser(@NonNull String user) {
        return getUser(user, GameMode.STANDARD, 1);
    }

    public OsuUser getUser(@NonNull String user, GameMode mode) {
        return getUser(user, mode, 1);
    }

    public OsuUser getUser(@NonNull String user, GameMode mode, int eventDays) {
        return getUser(new EndpointUsers.ArgumentsBuilder(user), mode, eventDays);
    }

    private OsuUser getUser(EndpointUsers.ArgumentsBuilder arguments, GameMode mode, int eventDays) {
        OsuUser user;
        try {
            user = osu.users.query(arguments.setMode(ObjectUtils.defaultIfNull(mode, GameMode.STANDARD))
                    .setEventDays(eventDays).build());
        } catch (OsuAPIException | RuntimeException e) {
            log.info("Failed to get user {}", arguments, e);
            return null;
        }
        if (user != null) {
            userNameCache.put(user.getID(), user.getUsername());
        }
        return user;
    }

    public List<OsuScore> getUserBest(@NonNull String user, GameMode mode) {
        try {
            return osu.userBests.query(new EndpointUserBests.ArgumentsBuilder(user)
                    .setMode(ObjectUtils.defaultIfNull(mode, GameMode.STANDARD)).setLimit(100).build());
        } catch (OsuAPIException | RuntimeException e) {
            log.info("Failed to get user best for {}", user, e);
            return null;
        }
    }

    public String getUserName(int userID) {
        String userName = userNameCache.get(userID);
        if (StringUtils.isEmpty(userName)) {
            OsuUser user = getUser(userID);
            return user == null ? Integer.toString(userID) : user.getUsername();
        } else {
            return userName;
        }
    }

    public List<OsuScore> getUserRecent(@NonNull String user, GameMode mode) {
        try {
            return osu.userRecents.query(new EndpointUserRecents.ArgumentsBuilder(user)
                    .setMode(ObjectUtils.defaultIfNull(mode, GameMode.STANDARD)).setLimit(50).build());
        } catch (OsuAPIException | RuntimeException e) {
            log.info("Failed to get user recent for {}", user, e);
            return null;
        }
    }

}
