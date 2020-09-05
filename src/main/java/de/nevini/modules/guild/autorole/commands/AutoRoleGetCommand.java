package de.nevini.modules.guild.autorole.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.autorole.jpa.AutoRoleData;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;

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
            EmbedBuilder builder = event.createGuildEmbedBuilder();
            builder.setTitle("Active auto-roles");
            for (AutoRoleData autoRole : autoRoles) {
                Role role = event.getGuild().getRoleById(autoRole.getRole());
                if (role != null) {
                    if ("join".equals(autoRole.getType())) {
                        builder.addField("Joins " + event.getGuild().getName(), role.getAsMention(), true);
                    } else if ("playing".equals(autoRole.getType())) {
                        builder.addField("Playing " + event.getGameService().getGameName(autoRole.getId()),
                                role.getAsMention(), true);
                    } else if ("plays".equals(autoRole.getType())) {
                        builder.addField("Plays " + event.getGameService().getGameName(autoRole.getId()),
                                role.getAsMention(), true);
                    } else if ("veteran".equals(autoRole.getType())) {
                        builder.addField("Veteran (" + autoRole.getId() + " days)", role.getAsMention(), true);
                    } else if ("voice".equals(autoRole.getType())) {
                        VoiceChannel channel = event.getGuild().getVoiceChannelById(autoRole.getId());
                        String channelName = channel == null ? autoRole.getId().toString() : channel.getName();
                        builder.addField("Voice (" + channelName + ")", role.getAsMention(), true);
                    } else {
                        log.warn("Unknown auto-role type: {}", autoRole.getType());
                    }
                }
            }
            event.reply(builder, event::complete);
        }
    }

}
