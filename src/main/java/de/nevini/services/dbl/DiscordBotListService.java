package de.nevini.services.dbl;

import de.nevini.locators.JdaProvider;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import org.discordbots.api.client.DiscordBotListAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Lazy;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DiscordBotListService {

    private final JDA jda;
    private final String token;

    private final Lazy<DiscordBotListAPI> api = new Lazy<>(this::initApi);

    public DiscordBotListService(
            @Autowired JdaProvider jdaProvider,
            @Value("${dbl.token}") String token
    ) {
        jda = jdaProvider.getJda();
        this.token = token;
    }

    private DiscordBotListAPI initApi() {
        return new DiscordBotListAPI.Builder()
                .botId(jda.getSelfUser().getId())
                .token(token)
                .build();
    }

    public void updateServerCount() {
        int shardId = jda.getShardInfo().getShardId();
        int shardTotal = jda.getShardInfo().getShardTotal();
        int serverCount = jda.getGuilds().size();
        log.info("Updating DBL server count for shard [{} / {}] to {}", shardId, shardTotal, serverCount);
        api.get().setStats(shardId, shardTotal, serverCount).exceptionally(e -> {
            log.error("Failed to update DBL server count", e);
            return null;
        });
    }

}
