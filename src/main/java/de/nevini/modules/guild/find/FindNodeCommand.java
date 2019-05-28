package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.common.NodeResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;

public class FindNodeCommand extends Command {

    public FindNodeCommand() {
        super(CommandDescriptor.builder()
                .keyword("node")
                .node(Node.GUILD_FIND_MODULE)
                .description("finds nodes by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        NodeResolver.describe().build()
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
