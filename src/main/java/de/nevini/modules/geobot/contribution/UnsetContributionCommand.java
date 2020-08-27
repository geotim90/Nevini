package de.nevini.modules.geobot.contribution;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.tribute.commands.TributeUnsetCommand;

public class UnsetContributionCommand extends Command {

    private static final TributeUnsetCommand delegate = new TributeUnsetCommand();

    public UnsetContributionCommand() {
        super(CommandDescriptor.builder()
                .keyword("contribution")
                .aliases(new String[]{"tribute", "donation"})
                .node(Node.GEOBOT_ADMIN)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **tribute unset**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
