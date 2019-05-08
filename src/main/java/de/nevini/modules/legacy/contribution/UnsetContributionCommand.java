package de.nevini.modules.legacy.contribution;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class UnsetContributionCommand extends Command {

    public UnsetContributionCommand() {
        super(CommandDescriptor.builder()
                .keyword("contribution")
                .node(Node.LEGACY_UNSET_CONTRIBUTION)
                .description("resets a user contribution")
                .syntax("<user>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
