package de.nevini.modules.guild.autorole.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.VoiceChannel;

@Slf4j
class AutoRoleUnsetVoiceCommand extends Command {

    AutoRoleUnsetVoiceCommand() {
        super(CommandDescriptor.builder()
                .keyword("voice")
                .node(Node.GUILD_AUTO_ROLE_SET)
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
