package de.nevini.services.dbl;

import de.nevini.locators.JdaProvider;
import net.dv8tion.jda.api.JDA;
import org.discordbots.api.client.DiscordBotListAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DiscordBotListService {

    private final JDA jda;
    private final DiscordBotListAPI api;

    public DiscordBotListService(
            @Autowired JdaProvider jdaProvider,
            @Value("{dbl.token}") String token
    ) {
        jda = jdaProvider.getJda();
        api = new DiscordBotListAPI.Builder()
                .botId(jda.getSelfUser().getId())
                .token(token)
                .build();
    }

    public void updateServerCount() {
        api.setStats(jda.getShardInfo().getShardId(), jda.getShardInfo().getShardTotal(), jda.getGuilds().size());
    }

}
