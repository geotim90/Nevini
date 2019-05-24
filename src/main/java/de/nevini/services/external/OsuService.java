package de.nevini.services.external;

import com.oopsjpeg.osu4j.GameMode;
import com.oopsjpeg.osu4j.OsuScore;
import com.oopsjpeg.osu4j.OsuUser;
import com.oopsjpeg.osu4j.backend.EndpointUserBests;
import com.oopsjpeg.osu4j.backend.EndpointUsers;
import com.oopsjpeg.osu4j.backend.Osu;
import com.oopsjpeg.osu4j.exception.OsuAPIException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class OsuService {

    private final Map<Integer, String> userNameCache = new ConcurrentHashMap<>();
    private final Map<Integer, String> beatmapNameCache = new ConcurrentHashMap<>();

    private final Osu osu;

    public OsuService(@Value("${osu.token:#{null}}") String token) {
        osu = Osu.getAPI(token);
    }

    public OsuUser getUser(int user) {
        return getUser(user, GameMode.STANDARD, 1);
    }

    public OsuUser getUser(int user, @NonNull GameMode mode) {
        return getUser(user, mode, 1);
    }

    public OsuUser getUser(int user, @NonNull GameMode mode, int eventDays) {
        return getUser(new EndpointUsers.ArgumentsBuilder(user), mode, eventDays);
    }

    public OsuUser getUser(@NonNull String user) {
        return getUser(user, GameMode.STANDARD, 1);
    }

    public OsuUser getUser(@NonNull String user, @NonNull GameMode mode) {
        return getUser(user, mode, 1);
    }

    public OsuUser getUser(@NonNull String user, @NonNull GameMode mode, int eventDays) {
        return getUser(new EndpointUsers.ArgumentsBuilder(user), mode, eventDays);
    }

    private OsuUser getUser(EndpointUsers.ArgumentsBuilder arguments, GameMode mode, int eventDays) {
        OsuUser osuUser;
        try {
            osuUser = osu.users.query(arguments.setMode(mode).setEventDays(eventDays).build());
        } catch (OsuAPIException | RuntimeException e) {
            log.info("Failed to get user {}", arguments, e);
            return null;
        }
        if (osuUser != null) {
            userNameCache.put(osuUser.getID(), osuUser.getUsername());
        }
        return osuUser;
    }

    public List<OsuScore> getUserBest(@NonNull String user, @NonNull GameMode mode, int limit) {
        try {
            return osu.userBests.query(new EndpointUserBests.ArgumentsBuilder(user).setMode(mode).setLimit(limit).build());
        } catch (OsuAPIException | RuntimeException e) {
            log.info("Failed to get user best for {}", user, e);
            return null;
        }
    }

    public String getUserName(int userId) {
        String userName = userNameCache.get(userId);
        if (userName == null) {
            return getUser(userId).getUsername();
        } else {
            return userName;
        }
    }

}
