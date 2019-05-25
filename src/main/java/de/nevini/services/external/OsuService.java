package de.nevini.services.external;

import com.oopsjpeg.osu4j.GameMode;
import com.oopsjpeg.osu4j.OsuBeatmap;
import com.oopsjpeg.osu4j.OsuScore;
import com.oopsjpeg.osu4j.OsuUser;
import com.oopsjpeg.osu4j.backend.*;
import com.oopsjpeg.osu4j.exception.OsuAPIException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class OsuService {

    private final Map<Integer, String> beatmapNameCache = new ConcurrentHashMap<>();
    private final Map<Integer, String> userNameCache = new ConcurrentHashMap<>();

    private final Osu osu;

    public OsuService(@Value("${osu.token:#{null}}") String token) {
        osu = Osu.getAPI(token);
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

    public OsuBeatmap getBeatmap(int beatmapId) {
        OsuBeatmap beatmap;
        try {
            beatmap = osu.beatmaps.query(new EndpointBeatmaps.ArgumentsBuilder().setBeatmapID(beatmapId).build()).get(0);
        } catch (OsuAPIException | RuntimeException e) {
            log.info("Failed to get beatmap {}", beatmapId, e);
            return null;
        }
        if (beatmap != null) {
            beatmapNameCache.put(beatmap.getID(), beatmap.getTitle());
        }
        return beatmap;
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
            user = osu.users.query(arguments.setMode(ObjectUtils.defaultIfNull(mode, GameMode.STANDARD)).setEventDays(eventDays).build());
        } catch (OsuAPIException | RuntimeException e) {
            log.info("Failed to get user {}", arguments, e);
            return null;
        }
        if (user != null) {
            userNameCache.put(user.getID(), user.getUsername());
        }
        return user;
    }

    public List<OsuScore> getUserBest(@NonNull String user, GameMode mode, int limit) {
        try {
            return osu.userBests.query(new EndpointUserBests.ArgumentsBuilder(user).setMode(ObjectUtils.defaultIfNull(mode, GameMode.STANDARD)).setLimit(limit).build());
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

    public List<OsuScore> getUserRecent(@NonNull String user, GameMode mode, int limit) {
        try {
            return osu.userRecents.query(new EndpointUserRecents.ArgumentsBuilder(user).setMode(ObjectUtils.defaultIfNull(mode, GameMode.STANDARD)).setLimit(limit).build());
        } catch (OsuAPIException | RuntimeException e) {
            log.info("Failed to get user recent for {}", user, e);
            return null;
        }
    }

}
