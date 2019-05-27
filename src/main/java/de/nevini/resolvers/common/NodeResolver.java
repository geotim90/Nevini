package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.scope.Node;
import de.nevini.util.Finder;

import java.util.List;
import java.util.regex.Pattern;

public class NodeResolver extends AbstractResolver<Node> {

    public static CommandOptionDescriptor.CommandOptionDescriptorBuilder describe() {
        return CommandOptionDescriptor.builder()
                .syntax("--node <node>")
                .description("Refers to a specific permission node for bot commands.")
                .keyword("--node")
                .aliases(new String[]{"//node", "-n", "/n"});
    }

    protected NodeResolver() {
        super("node", new Pattern[]{Pattern.compile("(?i)(?:(?:--|//)node|[-/]n)(?:\\s+(.+))?")});
    }

    @Override
    public List<Node> findSorted(CommandEvent ignore, String query) {
        return Finder.findAny(Node.values(), node -> new String[]{node.getNode(), node.name(), node.name().replace('_', ' ')}, query);
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
