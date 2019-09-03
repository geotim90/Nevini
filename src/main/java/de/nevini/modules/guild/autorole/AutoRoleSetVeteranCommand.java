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

@Slf4j
class AutoRoleSetVeteranCommand extends Command {

    AutoRoleSetVeteranCommand() {
        super(CommandDescriptor.builder()
                .keyword("veteran")
                .node(Node.GUILD_AUTO_ROLE_SET)
                .minimumBotPermissions(Permissions.sum(Permissions.BOT_EMBED, Permissions.MANAGE_ROLES))
                .description("configures auto-roles for users that joined the server some time ago")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.DURATION.describe(false, true),
                        Resolvers.ROLE.describe()
                })
                .details("Restrictions on who can be assigned veteran roles may be applied on node **"
                        + Node.GUILD_AUTO_ROLE_VETERAN.getNode() + "** (only server level overrides are applicable).")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.DURATION.resolveArgumentOrOptionOrInput(event, duration -> acceptDuration(event, duration));
    }

    private void acceptDuration(CommandEvent event, Long duration) {
        Resolvers.ROLE.resolveOptionOrInput(event, role -> acceptRole(event, duration, role));
    }

    private void acceptRole(CommandEvent event, Long duration, Role role) {
        event.getAutoRoleService().setVeteranAutoRole(duration, role);
        event.reply(CommandReaction.OK, event::complete);
    }

}
