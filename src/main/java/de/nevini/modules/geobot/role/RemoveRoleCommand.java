package de.nevini.modules.geobot.role;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

public class RemoveRoleCommand extends Command {

    public RemoveRoleCommand() {
        super(CommandDescriptor.builder()
                .keyword("role")
                .children(new Command[]{
                        new RemoveRoleInitiateCommand(),
                        new RemoveRoleMemberCommand(),
                        new RemoveRoleModCommand(),
                        new RemoveRoleAdminCommand(),
                })
                .description("Geobot style `remove role` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
