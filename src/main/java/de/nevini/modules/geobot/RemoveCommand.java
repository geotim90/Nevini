package de.nevini.modules.geobot;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
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
