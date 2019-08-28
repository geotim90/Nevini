package de.nevini;

import de.nevini.locators.JdaProvider;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.security.auth.login.LoginException;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class Application implements JdaProvider {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Getter
    private JDA jda;

    public Application(
            @Autowired EventDispatcher eventDispatcher,
            @Value("${bot.active:false}") boolean active,
            @Value("${bot.token:#{null}}") String token,
            @Value("${bot.shard:0}") int shard,
            @Value("${bot.shards:1}") int shards
    ) throws LoginException {
        if (active && token != null && shards > 0) {
            log.info("Configuring JDABuilder");
            JDABuilder shardBuilder = new JDABuilder();

            log.info("Authenticating using token");
            shardBuilder.setToken(token);

            log.info("Registering event dispatcher");
            shardBuilder.addEventListeners(eventDispatcher);

            log.info("Building shard {} ({} of {})", shard, shard + 1, shards);
            jda = shardBuilder.useSharding(shard, shards).build();
        } else if (!active) {
            log.warn("Not building any shards because bot.active:false");
        } else if (token == null) {
            log.warn("Not building any shards because bot.token:null");
        } else {
            log.warn("Not building any shards because bot.shards:{}", shards);
        }
    }

}
