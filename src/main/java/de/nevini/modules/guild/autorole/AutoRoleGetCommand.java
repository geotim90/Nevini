package de.nevini.modules.guild.autorole;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.jpa.autorole.AutoRoleData;
import de.nevini.scope.Node;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;

import java.util.Collection;

@Slf4j
class AutoRoleGetCommand extends Command {

    AutoRoleGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.GUILD_AUTO_ROLE_GET)
                .description("displays the currently configured auto-roles")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Collection<AutoRoleData> autoRoles = event.getAutoRoleService().getAutoRoles(event.getGuild());
        if (autoRoles.isEmpty()) {
            event.reply("There are currently no auto-roles configured for this server.", event::complete);
        } else {
            EmbedBuilder builder = event.createEmbedBuilder();
            builder.setTitle("Active auto-roles");
            for (AutoRoleData autoRole : autoRoles) {
                Role role = event.getGuild().getRoleById(autoRole.getRole());
                if (role != null) {
                    if ("join".equals(autoRole.getType())) {
                        builder.addField("Joins " + event.getGuild().getName(), role.getName(), true);
                    } else if ("playing".equals(autoRole.getType())) {
                        builder.addField("Playing " + event.getGameService().getGameName(autoRole.getId()),
                                role.getName(), true);
                    } else if ("plays".equals(autoRole.getType())) {
                        builder.addField("Plays " + event.getGameService().getGameName(autoRole.getId()),
                                role.getName(), true);
                    } else if ("veteran".equals(autoRole.getType())) {
                        builder.addField("Veteran (" + autoRole.getId() + " days)", role.getName(), true);
                    } else {
                        log.warn("Unknown auto-role type: {}", autoRole.getType());
                    }
                }
            }
            event.reply(builder, event::complete);
        }
    }

}
