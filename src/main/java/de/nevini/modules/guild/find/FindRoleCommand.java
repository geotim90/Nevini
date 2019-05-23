package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.modules.Node;
import de.nevini.resolvers.StringResolver;
import de.nevini.resolvers.common.Resolvers;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;

public class FindRoleCommand extends Command {

    private static final StringResolver nameResolver = new StringResolver("name or ID", "name");

    public FindRoleCommand() {
        super(CommandDescriptor.builder()
                .keyword("role")
                .aliases(new String[]{"r"})
                .node(Node.GUILD_FIND_ROLE)
                .description("finds roles by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("[--name] <name>")
                                .description("Any part of the name or ID of roles to look for. The flag is optional if this option is provided first.")
                                .keyword("--name")
                                .aliases(new String[]{"//name"})
                                .build()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        nameResolver.resolveArgumentOrOptionOrInput(event, name -> acceptName(event, name));
    }

    private void acceptName(CommandEvent event, String name) {
        List<Role> roles = Resolvers.ROLE.findSorted(event, name);
        if (roles.isEmpty()) {
            event.reply("I could not find any roles that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            roles.forEach(role -> embed.addField(role.getName(), role.getId(), true));
            event.reply(embed, event::complete);
        }
    }

}
