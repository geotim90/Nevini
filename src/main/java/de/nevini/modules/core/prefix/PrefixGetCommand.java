package de.nevini.modules.core.prefix;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class PrefixGetCommand extends Command {

    public PrefixGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.CORE_PREFIX_GET)
                .description("displays the currently configured command prefix")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(event.getPrefixService().getGuildPrefix(event.getGuild()), event::complete);
    }

}
