package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.StringResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.util.List;

public class FindPermissionCommand extends Command {

    private static final StringResolver nameResolver = new StringResolver("name", "name");

    public FindPermissionCommand() {
        super(CommandDescriptor.builder()
                .keyword("permission")
                .node(Node.GUILD_FIND_MODULE)
                .description("finds permission by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("[--name] <name>")
                                .description("Any part of the name of permission to look for. The flag is optional if this option is provided first.")
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
        List<Permission> permissions = Resolvers.PERMISSION.findSorted(event, name);
        if (permissions.isEmpty()) {
            event.reply("I could not find any permissions that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            permissions.forEach(permission -> embed.addField(permission.getName(), "", true));
            event.reply(embed, event::complete);
        }
    }

}
