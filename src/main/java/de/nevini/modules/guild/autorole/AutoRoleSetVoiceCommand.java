package de.nevini.modules.guild.autorole;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;

@Slf4j
class AutoRoleSetVoiceCommand extends Command {

    AutoRoleSetVoiceCommand() {
        super(CommandDescriptor.builder()
                .keyword("voice")
                .node(Node.GUILD_AUTO_ROLE_SET)
                .minimumBotPermissions(Permissions.sum(Permissions.BOT_EMBED, Permissions.MANAGE_ROLES))
                .description("configures auto-roles for users that are currently in a specific voice channel")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.VOICE_CHANNEL.describe(),
                        Resolvers.ROLE.describe(false, true)
                })
                .details("Restrictions on who can be assigned voice roles may be applied on node **"
                        + Node.GUILD_AUTO_ROLE_VOICE.getNode() + "** (only server level overrides are applicable).")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.VOICE_CHANNEL.resolveOptionOrInput(event, channel -> acceptChannel(event, channel));
    }

    private void acceptChannel(CommandEvent event, VoiceChannel channel) {
        Resolvers.ROLE.resolveArgumentOrOptionOrInput(event, role -> acceptGameAndRole(event, channel, role));
    }

    private void acceptGameAndRole(CommandEvent event, VoiceChannel channel, Role role) {
        event.getAutoRoleService().setVoiceAutoRole(channel, role);
        event.reply(CommandReaction.OK, event::complete);
    }

}
