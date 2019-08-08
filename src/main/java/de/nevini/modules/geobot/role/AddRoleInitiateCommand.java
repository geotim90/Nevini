package de.nevini.modules.geobot.role;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.guild.tribute.TributeRoleSetCommand;
import de.nevini.scope.Node;

class AddRoleInitiateCommand extends Command {

    private static TributeRoleSetCommand delegate = new TributeRoleSetCommand();

    AddRoleInitiateCommand() {
        super(CommandDescriptor.builder()
                .keyword("initiate")
                .node(Node.GEOBOT_ADMIN)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **tribute role set**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
