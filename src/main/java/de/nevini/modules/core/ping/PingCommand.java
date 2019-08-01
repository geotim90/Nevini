package de.nevini.modules.core.ping;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
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
                .minimumBotPermissions(Permissions.TALK)
                .description("measures the bot's communication latency")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Ping: ...", message -> message.editMessage("Ping: "
                + event.getMessage().getTimeCreated().until(message.getTimeCreated(), ChronoUnit.MILLIS)
                + "ms | Websocket: " + event.getJDA().getGatewayPing() + "ms").queue(event::complete));
    }

}
