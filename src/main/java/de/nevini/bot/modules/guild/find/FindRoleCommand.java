package de.nevini.bot.modules.guild.find;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.command.CommandOptionDescriptor;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

public class FindRoleCommand extends Command {

    public FindRoleCommand() {
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
