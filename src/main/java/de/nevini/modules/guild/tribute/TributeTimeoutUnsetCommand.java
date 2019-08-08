package de.nevini.modules.guild.tribute;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
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
