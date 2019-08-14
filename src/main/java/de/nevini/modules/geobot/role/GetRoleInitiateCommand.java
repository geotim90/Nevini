package de.nevini.modules.geobot.role;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.guild.tribute.TributeRoleGetCommand;
import de.nevini.scope.Node;

class GetRoleInitiateCommand extends Command {

    private static final TributeRoleGetCommand delegate = new TributeRoleGetCommand();

    GetRoleInitiateCommand() {
        super(CommandDescriptor.builder()
                .keyword("initiate")
                .node(Node.GEOBOT_ADMIN)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **tribute role get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
