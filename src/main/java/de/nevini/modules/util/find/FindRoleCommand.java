package de.nevini.modules.util.find;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

class FindRoleCommand extends Command {

    FindRoleCommand() {
        super(CommandDescriptor.builder()
                .keyword("role")
                .aliases(new String[]{"roles", "r"})
                .node(Node.UTIL_FIND_ROLE)
                .description("finds roles by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.ROLE.describe(true, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.ROLE.resolveListArgumentOrOptionOrInput(event, roles -> acceptRoles(event, roles));
    }

    private void acceptRoles(CommandEvent event, List<Role> roles) {
        if (roles.isEmpty()) {
            event.reply("I could not find any roles that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createGuildEmbedBuilder();
            roles.forEach(role -> embed.addField(role.getName(), role.getId(), true));
            event.reply(embed, event::complete);
        }
    }

}
