package de.nevini.bot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import de.nevini.services.PrefixService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Component
public class CommandClientFactory {

    private final ApplicationContext applicationContext;
    private final PrefixService prefixService;
    private final String ownerId;
    private final String serverInvite;

    public CommandClientFactory(
            @Autowired ApplicationContext applicationContext,
            @Autowired PrefixService prefixService,
            @Value("${bot.owner.id}") String ownerId,
            @Value("${bot.server.invite}") String serverInvite
    ) {
        this.applicationContext = applicationContext;
        this.prefixService = prefixService;
        this.ownerId = ownerId;
        this.serverInvite = serverInvite;
    }

    public CommandClient createCommandClient() {
        final CommandClientBuilder builder = new CommandClientBuilder();
        configureOwnerId(builder);
        configurePrefix(builder);
        configureServerInvite(builder);
        configureEmojis(builder);
        configureCommands(builder);
        return builder.build();
    }

    private void configureOwnerId(CommandClientBuilder builder) {
        builder.setOwnerId(ownerId);
    }

    private void configurePrefix(CommandClientBuilder builder) {
        builder.setPrefix(prefixService.getDefaultPrefix());
        builder.setAlternativePrefix('@' + prefixService.getDefaultName());
        builder.setGuildSettingsManager(this::createGuildPrefixProvider);
    }

    private void configureServerInvite(CommandClientBuilder builder) {
        builder.setServerInvite(serverInvite);
    }

    private void configureEmojis(CommandClientBuilder builder) {
        builder.setEmojis("✅", "⚠️", "❌");
    }

    private void configureCommands(CommandClientBuilder builder) {
        final Map<String, Object> shardEventListeners = applicationContext.getBeansWithAnnotation(CommandComponent.class);
        shardEventListeners.values().stream()
                .filter(e -> e instanceof Command)
                .forEach(command -> builder.addCommand((Command) command));
    }

    private GuildSettingsProvider createGuildPrefixProvider(Guild guild) {
        return new GuildSettingsProvider() {
            @Override
            public Collection<String> getPrefixes() {
                return prefixService.getGuildPrefixes(guild);
            }
        };
    }

}
