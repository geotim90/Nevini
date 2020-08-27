package de.nevini.modules.guild.tribute.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandReaction;

public class TributeTimeoutUnsetCommand extends Command {

    public TributeTimeoutUnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .aliases(new String[]{"remove", "clear", "reset"})
                .node(Node.GUILD_TRIBUTE_TIMEOUT_SET)
                .description("removes the configured timeout for users that need to contribute")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getTributeService().unsetTimeout(event.getGuild());
        event.reply(CommandReaction.OK, event::complete);
    }

}
