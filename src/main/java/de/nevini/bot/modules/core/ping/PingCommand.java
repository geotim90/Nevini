package de.nevini.bot.modules.core.ping;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.scope.Node;
import de.nevini.bot.scope.Permissions;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class PingCommand extends Command {

    public PingCommand() {
        super(CommandDescriptor.builder()
                .keyword("ping")
                .aliases(new String[]{"pong", "latency"})
                .guildOnly(false)
                .node(Node.CORE_PING)
                .minimumBotPermissions(Permissions.NONE)
                .minimumUserPermissions(Permissions.NONE)
                .description("measures the bot's communication latency")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Ping: ...", message -> message.editMessage("Ping: "
                + event.getMessage().getCreationTime().until(message.getCreationTime(), ChronoUnit.MILLIS)
                + "ms | Websocket: " + event.getJDA().getPing() + "ms").queue(event::complete));
    }

}
