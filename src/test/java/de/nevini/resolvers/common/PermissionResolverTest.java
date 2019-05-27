package de.nevini.resolvers.common;

import net.dv8tion.jda.core.Permission;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class PermissionResolverTest {

    @Test
    public void findMessageRead() {
        ArrayList<Permission> expected = new ArrayList<>(1);
        expected.add(Permission.MESSAGE_READ);
        // needs to match the name (case-insensitive)
        assertEquals(expected, Resolvers.PERMISSION.findSorted(null, "read messages"));
        // needs to match the enum const name (case-insensitive)
        assertEquals(expected, Resolvers.PERMISSION.findSorted(null, "message_read"));
        // needs to match the enum const name without underscores (case-insensitive)
        assertEquals(expected, Resolvers.PERMISSION.findSorted(null, "message read"));
    }

}
