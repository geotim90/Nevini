package de.nevini.services.external;

import com.oopsjpeg.osu4j.GameMode;
import com.oopsjpeg.osu4j.OsuUser;
import com.oopsjpeg.osu4j.backend.EndpointUsers;
import com.oopsjpeg.osu4j.backend.Osu;
import com.oopsjpeg.osu4j.exception.OsuAPIException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OsuService {

    private final Osu osu;

    public OsuService(@Value("${osu.token:#{null}}") String token) {
        osu = Osu.getAPI(token);
    }

    public OsuUser getUser(String user) {
        try {
            return osu.users.query(new EndpointUsers.ArgumentsBuilder(user).build());
        } catch (OsuAPIException e) {
            log.info("Failed to get user {}", user, e);
            return null;
        }
    }

    public OsuUser getUser(String user, GameMode mode) {
        try {
            return osu.users.query(new EndpointUsers.ArgumentsBuilder(user).setMode(mode).build());
        } catch (OsuAPIException e) {
            log.info("Failed to get user {} with mode {}", user, mode, e);
            return null;
        }
    }

}
