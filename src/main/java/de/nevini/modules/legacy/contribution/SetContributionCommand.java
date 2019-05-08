package de.nevini.modules.legacy.contribution;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class SetContributionCommand extends Command {

    public SetContributionCommand() {
        super(CommandDescriptor.builder()
                .keyword("contribution")
                .node(Node.LEGACY_SET_CONTRIBUTION)
                .description("confirms that a user has made a contribution")
                .syntax("<user>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
