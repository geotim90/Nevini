package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;

public class UnsetTimeoutContributionCommand extends Command {

    public UnsetTimeoutContributionCommand() {
        super(CommandDescriptor.builder()
                .keyword("contribution")
                .node(Node.LEGACY_SET_TIMEOUT)
                .description("removes the contribution timeout")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getLegacyTimeoutService().removeContributionTimeout(event.getGuild());
        event.reply(CommandReaction.OK, "Removed timeout for **contribution**.");
    }

}
