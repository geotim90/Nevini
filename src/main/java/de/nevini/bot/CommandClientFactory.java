package de.nevini.bot;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import de.nevini.services.PrefixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

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
        CommandClientBuilder builder = new CommandClientBuilder();
        configureOwnerId(builder);
        configurePrefix(builder);
        configureHelp(builder);
        configureServerInvite(builder);
        configureEmojis(builder);
        configureGame(builder);
        configureCommands(builder);
        configureListener(builder);
        return builder.build();
    }

    private void configureOwnerId(CommandClientBuilder builder) {
        builder.setOwnerId(ownerId);
    }

    private void configureHelp(CommandClientBuilder builder) {
        builder.useHelpBuilder(false);
    }

    private void configureServerInvite(CommandClientBuilder builder) {
        builder.setServerInvite(serverInvite);
    }

    private void configureEmojis(CommandClientBuilder builder) {
        builder.setEmojis("✅", "⚠️", "❌");
    }

    private void configureGame(CommandClientBuilder builder) {
        builder.setGame(null);
    }

    private void configureCommands(CommandClientBuilder builder) {
        Map<String, AbstractCommand> commands = applicationContext.getBeansOfType(AbstractCommand.class);
        log.info("Adding commands: {}", commands.values().stream().map(e -> e.getClass().getSimpleName()).collect(Collectors.joining(", ")));
        commands.values().forEach(builder::addCommand);
    }

    private void configureListener(CommandClientBuilder builder) {
        builder.setListener(new CommandLogger());
    }

    private void configurePrefix(CommandClientBuilder builder) {
        builder.setGuildSettingsManager(guild -> new GuildSettingsProvider() {
            @Override
            public Collection<String> getPrefixes() {
                return Collections.singleton(prefixService.getGuildPrefix(guild));
            }
        });
    }

}
