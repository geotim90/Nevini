package de.nevini.modules.core.prefix;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class PrefixGetCommand extends Command {

    public PrefixGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "print", "show"})
                .module(Module.CORE)
                .node(Node.CORE_PREFIX_GET)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("displays the command prefix")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(event.getPrefixService().getGuildPrefix(event.getGuild()));
    }

}
