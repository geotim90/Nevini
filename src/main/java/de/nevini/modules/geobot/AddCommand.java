package de.nevini.modules.geobot;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.geobot.role.AddRoleCommand;

class AddCommand extends Command {

    AddCommand() {
        super(CommandDescriptor.builder()
                .keyword("add")
                .children(new Command[]{
                        new AddRoleCommand()
                })
                .description("Geobot style `add` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // do nothing
    }

}
