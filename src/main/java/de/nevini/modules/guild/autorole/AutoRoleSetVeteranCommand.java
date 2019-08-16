package de.nevini.modules.guild.autorole;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.StringResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Role;
import org.apache.commons.lang3.StringUtils;

@Slf4j
class AutoRoleSetVeteranCommand extends Command {

    private static final StringResolver durationResolver = new StringResolver("duration", "duration",
            CommandOptionDescriptor.builder()
                    .syntax("[--duration] <days>")
                    .description("The number of days after which a user is considered a veteran. The flag is optional.")
                    .keyword("--duration")
                    .aliases(new String[]{"//duration", "--days", "//days", "-d", "/d"})
                    .build());

    AutoRoleSetVeteranCommand() {
        super(CommandDescriptor.builder()
                .keyword("veteran")
                .node(Node.GUILD_AUTO_ROLE_SET)
                .minimumBotPermissions(Permissions.sum(Permissions.BOT_EMBED, Permissions.MANAGE_ROLES))
                .description("configures auto-roles for users that joined the server some time ago")
                .options(new CommandOptionDescriptor[]{
                        durationResolver.describe(),
                        Resolvers.ROLE.describe()
                })
                .details("Restrictions on who can be assigned veteran roles may be applied on node **"
                        + Node.GUILD_AUTO_ROLE_VETERAN.getNode() + "** (only server level overrides are applicable).")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        durationResolver.resolveArgumentOrOptionOrInput(event, duration -> acceptDuration(event, duration));
    }

    private void acceptDuration(CommandEvent event, String duration) {
        // validate duration
        int days;
        if (StringUtils.isEmpty(duration)) {
            // command was aborted or no input provided
            event.reply(CommandReaction.DEFAULT_NOK, event::complete);
            return;
        } else {
            try {
                // try to parse input
                days = Integer.parseInt(duration);
            } catch (NumberFormatException e) {
                // failed to parse duration
                event.reply(CommandReaction.WARNING, "You did not provide a valid duration!", event::complete);
                return;
            }
            if (days < 1) {
                event.reply(CommandReaction.WARNING, "The duration cannot be less than 1!", event::complete);
                return;
            }
        }

        Resolvers.ROLE.resolveOptionOrInput(event, role -> acceptRole(event, days, role));
    }

    private void acceptRole(CommandEvent event, int days, Role role) {
        event.getAutoRoleService().setVeteranAutoRole(days, role);
        event.reply(CommandReaction.OK, event::complete);
    }

}
