package de.nevini.modules.geobot.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.guild.tribute.TributeTimeoutGetCommand;
import de.nevini.scope.Node;

class GetTimeoutContributionCommand extends Command {

    private static final TributeTimeoutGetCommand delegate = new TributeTimeoutGetCommand();

    GetTimeoutContributionCommand() {
        super(CommandDescriptor.builder()
                .keyword("contribution")
                .aliases(new String[]{"tribute", "donation"})
                .node(Node.GEOBOT_MOD)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **tribute timeout get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
