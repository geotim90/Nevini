package de.nevini.modules.admin.status;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import de.nevini.util.Formatter;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

@Component
public class StatusCommand extends Command {

    public StatusCommand() {
        super(CommandDescriptor.builder()
                .keyword("status")
                .aliases(new String[]{"health", "health-check"})
                .ownerOnly(true)
                .guildOnly(false)
                .node(Node.CORE_HELP) // dummy node
                .minimumBotPermissions(Permissions.TALK)
                .description("gives a short report on the status of the bot")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        StringBuilder builder = new StringBuilder();

        // output JDA statistics
        builder.append("**Guilds on this shard**: ").append(event.getJDA().getGuilds().size());

        // output JVM statistics
        builder.append("\n\n**Start time**: ")
                .append(Formatter.formatTimestamp(ManagementFactory.getRuntimeMXBean().getStartTime()));
        builder.append("\n\n**Uptime**: ").append(Formatter.formatUnits(ManagementFactory.getRuntimeMXBean().getUptime()));
        builder.append("\n\n**Active threads**: ").append(Thread.activeCount());

        // output memory statistics
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = Runtime.getRuntime().maxMemory();
        builder.append("\n\n**Memory**:\n```");
        builder.append(String.format("\nTotal memory: %15s bytes", Formatter.formatInteger(totalMemory)));
        builder.append(String.format("\nUsed memory:  %15s bytes", Formatter.formatInteger(usedMemory)));
        builder.append(String.format("\nFree memory:  %15s bytes", Formatter.formatInteger(freeMemory)));
        builder.append(String.format("\nMax memory:   %15s bytes", Formatter.formatInteger(maxMemory)));
        builder.append("\n```");

        event.reply(builder.toString(), event::complete);
    }

}
