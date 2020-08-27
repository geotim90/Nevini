package de.nevini.modules.geobot;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.modules.geobot.role.RemoveRoleCommand;

class RemoveCommand extends Command {

    RemoveCommand() {
        super(CommandDescriptor.builder()
                .keyword("remove")
                .children(new Command[]{
                        new RemoveRoleCommand()
                })
                .description("Geobot style `remove` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // do nothing
    }

}
