package de.nevini.bot;

import de.nevini.commons.concurrent.EventDispatcher;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.login.LoginException;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public Application(
            @Autowired EventDispatcher<Event> eventDispatcher,
            @Value("${bot.active:false}") boolean active,
            @Value("${bot.token:#{null}}") String token,
            @Value("${bot.shards:1}") int shards
    ) throws LoginException {
        if (active && token != null && shards > 0) {
            log.info("Configuring JDABuilder");
            JDABuilder shardBuilder = new JDABuilder();
            shardBuilder.setAudioEnabled(false);

            log.info("Authenticating using token");
            shardBuilder.setToken(token);

            log.info("Registering event dispatcher");
            shardBuilder.addEventListener(eventDispatcher);

            for (int shard = 0; shard < shards; ++shard) {
                log.info("Building shard {} ({} of {})", shard, shard + 1, shards);
                shardBuilder.useSharding(shard, shards).build();
            }
        } else if (!active) {
            log.warn("Not building any shards because jda.active:false");
        } else if (token == null) {
            log.warn("Not building any shards because jda.token:null");
        } else {
            log.warn("Not building any shards because jda.shards:{}", shards);
        }
    }

}
