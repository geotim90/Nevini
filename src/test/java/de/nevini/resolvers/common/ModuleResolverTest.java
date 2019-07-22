package de.nevini.resolvers.common;

import de.nevini.scope.Module;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ModuleResolverTest {

    @Test
    public void findCore() {
        ArrayList<Module> expected = new ArrayList<>(1);
        expected.add(Module.CORE);
        // needs to match the name (case-insensitive)
        assertEquals(expected, Resolvers.MODULE.findSorted(null, "core"));
    }

}
