package de.nevini.bot.resolvers.common;

import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.command.CommandOptionDescriptor;
import de.nevini.bot.resolvers.AbstractResolver;
import de.nevini.bot.scope.Node;
import de.nevini.bot.util.Finder;

import java.util.List;
import java.util.regex.Pattern;

public class NodeResolver extends AbstractResolver<Node> {

    protected NodeResolver() {
        super("node", new Pattern[]{Pattern.compile("(?i)(?:(?:--|//)node|[-/]n)(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--node] <node>" : "--node <node>")
                .description("Refers to " + (list
                        ? "all permission nodes for bot commands"
                        : "a specific permission node for bot commands")
                        + " with a matching name."
                        + (argument ? "\nThe `--node` flag is optional if this option is provided first." : ""))
                .keyword("--node")
                .aliases(new String[]{"//node", "-n", "/n"})
                .build();
    }

    @Override
    public List<Node> findSorted(CommandEvent ignore, String query) {
        return Finder.findAny(Node.values(), node -> new String[]{node.getNode(), node.name(),
                node.name().replace('_', ' ')}, query);
    }

    @Override
    protected String getFieldNameForPicker(Node item) {
        return item.getNode();
    }

    @Override
    protected String getFieldValueForPicker(Node item) {
        return "";
    }

}
