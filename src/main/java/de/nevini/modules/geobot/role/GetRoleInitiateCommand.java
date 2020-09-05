package de.nevini.modules.geobot.role;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.tribute.commands.TributeRoleGetCommand;

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
