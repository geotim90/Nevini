package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.scope.Module;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ModuleResolverTest {

    @Test
    public void findCore() {
        ArrayList<Module> expected = new ArrayList<>(1);
        expected.add(Module.CORE);
        // mock event
        CommandEvent event = Mockito.mock(CommandEvent.class);
        // needs to match the name (case-insensitive)
        assertEquals(expected, Resolvers.MODULE.findSorted(event, "core"));
    }

}
