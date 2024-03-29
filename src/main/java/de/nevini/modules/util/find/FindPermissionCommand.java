package de.nevini.modules.util.find;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.List;

class FindPermissionCommand extends Command {

    FindPermissionCommand() {
        super(CommandDescriptor.builder()
                .keyword("permission")
                .aliases(new String[]{"permissions", "perm", "perms", "p"})
                .guildOnly(false)
                .node(Node.UTIL_FIND_PERMISSION)
                .description("finds permission by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.PERMISSION.describe(true, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.PERMISSION.resolveListArgumentOrOptionOrInput(event,
                permissions -> acceptPermissions(event, permissions));
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
