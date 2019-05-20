package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class PermissionCommand extends Command {

    public PermissionCommand() {
        super(CommandDescriptor.builder()
                .keyword("permission")
                .aliases(new String[]{"permissions", "perm", "perms"})
                .children(new Command[]{
                        new PermissionGetCommand(),
                        new PermissionDebugCommand(),
                        new PermissionAllowCommand(),
                        new PermissionDenyCommand(),
                        new PermissionResetCommand()
                })
                .description("configures permission node overrides for bot commands")
                .details("__Options__\n"
                        + "**--server** - for the whole server instead of a specific channel\n"
                        + "**--permission <permission>** - for users that have a specific effective permission (e.g. \"Manage Permissions\") on the server\n"
                        + "**--role <role>** - for users that have a specific role on the server\n"
                        + "**--user <user>** - for a specific user on the server\n"
                        + "**--channel <channel>** - for a specific channel\n"
                        + "**--channel <channel> --permission <permission>** - for users that have a specific effective permission (e.g. \"Manage Permissions\") in a specific channel\n"
                        + "**--channel <channel> --role <role>** - for users that have a specific role in a specific channel\n"
                        + "**--channel <channel> --user <user>** - for a specific user in a specific channel\n\n"
                        + "Permission node overrides for bot commands are applied in the order listed above, with the default permissions being applied first."
                        + "If multiple overrides on the same \"level\" disagree with each other (e.g. multiple roles), **allow** will trump **deny**."
                        + "Server owners and administrators are never restricted by permission node overrides."
                        + "Users cannot configure permissions for roles above their own or configure permission nodes they do not have themselves."
                        + "Users cannot configure permissions for users with roles above their own or configure permission nodes they do not have themselves."
                )
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
