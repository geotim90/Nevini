package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.common.ModuleService;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class NodeResolverTest {

    @Test
    public void findCoreHelp() {
        // mock module service
        ModuleService moduleService = Mockito.mock(ModuleService.class);
        Mockito.when(moduleService.isModuleActive(Mockito.any(), Mockito.any())).thenReturn(true);
        // mock event
        CommandEvent event = Mockito.mock(CommandEvent.class);
        Mockito.when(event.getModuleService()).thenReturn(moduleService);
        // set expected result
        ArrayList<Node> expected = new ArrayList<>(1);
        expected.add(Node.CORE_HELP);
        // needs to match the node (case-insensitive)
        assertEquals(expected, Resolvers.NODE.findSorted(event, "core.help"));
        // needs to match the enum const name (case-insensitive)
        assertEquals(expected, Resolvers.NODE.findSorted(event, "core_help"));
        // needs to match the enum const name without underscores (case-insensitive)
        assertEquals(expected, Resolvers.NODE.findSorted(event, "core help"));
    }

}
