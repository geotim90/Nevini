package de.nevini.resolvers;

import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;
import de.nevini.util.Finder;
import lombok.NonNull;

import java.util.List;
import java.util.regex.Pattern;

public class NodeResolver extends AbstractResolver<Node> {

    public NodeResolver() {
        super("node", new Pattern[]{Pattern.compile("(?i)(?:(?:--|//)node|[-/]n)(?:\\s+(.+))?")});
    }

    @Override
    protected List<Node> findSorted(@NonNull CommandEvent event, String query) {
        return Finder.find(Node.values(), Node::getNode, query);
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