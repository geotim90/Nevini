package de.nevini.bot.modules.guild.find;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandOptionDescriptor;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;

class FindNodeCommand extends Command {

    FindNodeCommand() {
        super(CommandDescriptor.builder()
                .keyword("node")
                .node(Node.GUILD_FIND_MODULE)
                .description("finds nodes by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.NODE.describe(true, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.NODE.resolveListArgumentOrOptionOrInput(event, nodes -> acceptNodes(event, nodes));
    }

    private void acceptNodes(CommandEvent event, List<Node> nodes) {
        if (nodes.isEmpty()) {
            event.reply("I could not find any nodes that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            nodes.forEach(node -> embed.addField(node.getNode(), "", true));
            event.reply(embed, event::complete);
        }
    }

}
