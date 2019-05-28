package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.common.PermissionResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.util.List;

public class FindPermissionCommand extends Command {

    public FindPermissionCommand() {
        super(CommandDescriptor.builder()
                .keyword("permission")
                .node(Node.GUILD_FIND_MODULE)
                .description("finds permission by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        PermissionResolver.describe().build()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.PERMISSION.resolveListArgumentOrOptionOrInput(event, permissions -> acceptPermissions(event, permissions));
    }

    private void acceptPermissions(CommandEvent event, List<Permission> permissions) {
        if (permissions.isEmpty()) {
            event.reply("I could not find any permissions that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            permissions.forEach(permission -> embed.addField(permission.getName(), "", true));
            event.reply(embed, event::complete);
        }
    }

}
