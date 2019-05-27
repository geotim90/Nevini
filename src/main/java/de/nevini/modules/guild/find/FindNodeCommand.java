package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.StringResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;

public class FindNodeCommand extends Command {

    private static final StringResolver nameResolver = new StringResolver("name", "name");

    public FindNodeCommand() {
        super(CommandDescriptor.builder()
                .keyword("node")
                .node(Node.GUILD_FIND_MODULE)
                .description("finds nodes by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("[--name] <name>")
                                .description("Any part of the name of nodes to look for. The flag is optional if this option is provided first.")
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
        List<Node> nodes = Resolvers.NODE.findSorted(event, name);
        if (nodes.isEmpty()) {
            event.reply("I could not find any nodes that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            nodes.forEach(node -> embed.addField(node.getNode(), "", true));
            event.reply(embed, event::complete);
        }
    }

}