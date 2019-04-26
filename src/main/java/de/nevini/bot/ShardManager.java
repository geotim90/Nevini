package de.nevini.bot;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.JDABuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Slf4j
@Component
public class ShardManager {

    public ShardManager(
            @Value("${jda.active:false}") boolean active,
            @Value("${jda.token:null}") String token,
            @Value("${jda.shards:1}") int shards,
            @Autowired CommandClientFactory commandClientFactory,
            @Autowired EventDispatcher eventDispatcher
    ) throws LoginException {
        if (active && token != null && shards > 0) {
            log.info("Configuring JDABuilder");
            JDABuilder shardBuilder = new JDABuilder();

            log.info("Authenticating using token");
            shardBuilder.setToken(token);

            log.info("Building command client");
            shardBuilder.addEventListener(commandClientFactory.createCommandClient());

            log.info("Adding event dispatcher");
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
