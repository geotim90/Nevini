package de.nevini.bot;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
public class ShardManager {

    public ShardManager(
            @Value("${jda.active:false}") boolean active,
            @Value("${jda.token:null}") String token,
            @Value("${jda.shards:1}") int shards,
            @Autowired ApplicationContext applicationContext
    ) throws LoginException {
        if (active && token != null && shards > 0) {
            log.info("Configuring JDABuilder");
            final JDABuilder shardBuilder = new JDABuilder();

            log.info("Authenticating using token");
            shardBuilder.setToken(token);

            shardBuilder.setEventManager(new AnnotatedEventManager());
            final Map<String, Object> shardEventListeners = applicationContext.getBeansWithAnnotation(ShardEventListener.class);
            log.info("Adding shard event listeners: {}", Arrays.toString(shardEventListeners.values().stream().map(e -> e.getClass().getSimpleName()).toArray()));
            shardEventListeners.forEach(shardBuilder::addEventListener);

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
