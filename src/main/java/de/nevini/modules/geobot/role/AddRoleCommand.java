package de.nevini.modules.geobot.role;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

public class AddRoleCommand extends Command {

    public AddRoleCommand() {
        super(CommandDescriptor.builder()
                .keyword("role")
                .children(new Command[]{
                        new AddRoleInitiateCommand(),
                        new AddRoleMemberCommand(),
                        new AddRoleModCommand(),
                        new AddRoleAdminCommand(),
                })
                .description("Geobot style `add role` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
