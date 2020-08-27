package de.nevini.modules.geobot.role;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.tribute.commands.TributeRoleUnsetCommand;

class RemoveRoleInitiateCommand extends Command {

    private static final TributeRoleUnsetCommand delegate = new TributeRoleUnsetCommand();

    RemoveRoleInitiateCommand() {
        super(CommandDescriptor.builder()
                .keyword("initiate")
                .node(Node.GEOBOT_ADMIN)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **tribute role unset**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
