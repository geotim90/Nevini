package de.nevini.bot.modules.core.prefix;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.scope.Node;
import de.nevini.bot.scope.Permissions;

class PrefixGetCommand extends Command {

    PrefixGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.CORE_PREFIX_GET)
                .minimumBotPermissions(Permissions.TALK)
                .description("displays the currently configured command prefix")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(event.getPrefixService().getGuildPrefix(event.getGuild()), event::complete);
    }

}
