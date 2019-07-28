package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.util.List;

class FindPermissionCommand extends Command {

    FindPermissionCommand() {
        super(CommandDescriptor.builder()
                .keyword("permission")
                .node(Node.GUILD_FIND_MODULE)
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
