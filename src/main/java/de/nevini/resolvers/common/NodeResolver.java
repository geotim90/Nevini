package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.resolvers.OptionResolver;
import de.nevini.scope.Node;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NodeResolver extends OptionResolver<Node> {

    NodeResolver() {
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
    public List<Node> findSorted(@NonNull CommandEvent event, String query) {
        return Finder.findAny(Arrays.stream(Node.values())
                        .filter(node -> node.getNode().startsWith(node.getModule().getName()))
                        .filter(node -> event.getModuleService().isModuleActive(event.getGuild(), node.getModule()))
                        .collect(Collectors.toList()),
                node -> new String[]{node.getNode(), node.name(), node.name().replace('_', ' ')},
                query);
    }

    @Override
    protected @NonNull String getFieldNameForPicker(Node item) {
        return item.getNode();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(Node item) {
        return "";
    }

}
