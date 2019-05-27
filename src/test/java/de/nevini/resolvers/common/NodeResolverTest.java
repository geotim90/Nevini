package de.nevini.resolvers.common;

import de.nevini.scope.Node;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class NodeResolverTest {

    @Test
    public void findCoreHelp() {
        ArrayList<Node> expected = new ArrayList<>(1);
        expected.add(Node.CORE_HELP);
        // needs to match the node (case-insensitive)
        assertEquals(expected, Resolvers.NODE.findSorted(null, "core.help"));
        // needs to match the enum const name (case-insensitive)
        assertEquals(expected, Resolvers.NODE.findSorted(null, "core_help"));
        // needs to match the enum const name without underscores (case-insensitive)
        assertEquals(expected, Resolvers.NODE.findSorted(null, "core help"));
    }

}
