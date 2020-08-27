package de.nevini.modules.geobot.role;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

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
