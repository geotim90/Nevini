package de.nevini.modules.guild.tribute;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import net.dv8tion.jda.api.entities.Role;

public class TributeRoleGetCommand extends Command {

    public TributeRoleGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.GUILD_TRIBUTE_ROLE_GET)
                .description("displays the role for users that need to contribute")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Role role = event.getTributeService().getRole(event.getGuild());
        if (role == null) {
            event.reply("No tribute role has been configured.", event::complete);
        } else {
            event.reply("The configured tribute role is **" + role.getName() + "**.", event::complete);
        }
    }

}
