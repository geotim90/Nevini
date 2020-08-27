package de.nevini.modules.util.find;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

class FindNodeCommand extends Command {

    FindNodeCommand() {
        super(CommandDescriptor.builder()
                .keyword("node")
                .aliases(new String[]{"nodes", "n"})
                .guildOnly(false)
                .node(Node.UTIL_FIND_MODULE)
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
