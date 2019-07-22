package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

class FindRoleCommand extends Command {

    FindRoleCommand() {
        super(CommandDescriptor.builder()
                .keyword("role")
                .aliases(new String[]{"r"})
                .node(Node.GUILD_FIND_ROLE)
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
            EmbedBuilder embed = event.createEmbedBuilder();
            roles.forEach(role -> embed.addField(role.getName(), role.getId(), true));
            event.reply(embed, event::complete);
        }
    }

}
