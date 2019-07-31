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
import net.dv8tion.jda.core.entities.VoiceChannel;

@Slf4j
class AutoRoleUnsetVoiceCommand extends Command {

    AutoRoleUnsetVoiceCommand() {
        super(CommandDescriptor.builder()
                .keyword("voice")
                .node(Node.GUILD_AUTO_ROLE_UNSET)
                .minimumBotPermissions(Permissions.sum(Permissions.BOT_EMBED, Permissions.MANAGE_ROLES))
                .description("stops auto-roles for users that are currently in a specific voice channel")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.GAME.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.VOICE_CHANNEL.resolveOptionOrInput(event, channel -> acceptChannel(event, channel));
    }

    private void acceptChannel(CommandEvent event, VoiceChannel channel) {
        event.getAutoRoleService().removeVoiceAutoRole(channel);
        event.reply(CommandReaction.OK, event::complete);
    }

}
