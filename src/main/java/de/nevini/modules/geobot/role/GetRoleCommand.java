package de.nevini.modules.geobot.role;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

public class GetRoleCommand extends Command {

    public GetRoleCommand() {
        super(CommandDescriptor.builder()
                .keyword("role")
                .aliases(new String[]{"roles"})
                .children(new Command[]{
                        new GetRoleInitiateCommand(),
                        new GetRoleMemberCommand(),
                        new GetRoleModCommand(),
                        new GetRoleAdminCommand(),
                })
                .description("Geobot style `get role` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
