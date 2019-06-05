package de.nevini.bot.modules.guild.find;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandOptionDescriptor;
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
