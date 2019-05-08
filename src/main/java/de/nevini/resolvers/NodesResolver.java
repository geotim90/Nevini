package de.nevini.resolvers;

import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;
import de.nevini.util.Finder;
import lombok.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class NodesResolver extends AbstractResolver<Collection<Node>> {

    public NodesResolver() {
        super("node", new Pattern[]{Pattern.compile("(?i)(?:(?:--|//)node|[-/]n)(?:\\s+(.+))")});
    }

    @Override
    protected List<Collection<Node>> findSorted(@NonNull CommandEvent event, String query) {
        Collection<Node> results = Finder.find(Node.values(), Node::getNode, query);
        if (results.isEmpty()) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList(results);
        }
    }

    @Override
    protected String getFieldNameForPicker(Collection<Node> item) {
        return item.toString();
    }

    @Override
    protected String getFieldValueForPicker(Collection<Node> item) {
        return null;
    }

}
