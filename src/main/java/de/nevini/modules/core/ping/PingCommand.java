package de.nevini.modules.core.ping;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class PingCommand extends Command {

    protected PingCommand() {
        super(CommandDescriptor.builder()
                .keyword("ping")
                .guildOnly(false)
                .module(Module.CORE)
                .description("measures the bot's latency")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Ping: ...", message -> message.editMessage("Ping: "
                + event.getMessage().getCreationTime().until(message.getCreationTime(), ChronoUnit.MILLIS)
                + "ms | Websocket: " + event.getJDA().getPing() + "ms").queue());
    }

}
