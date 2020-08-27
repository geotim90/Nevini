package de.nevini.core.resolvers.common;

import de.nevini.core.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class PermissionResolverTest {

    @Test
    public void findMessageRead() {
        ArrayList<Permission> expected = new ArrayList<>(1);
        expected.add(Permission.MESSAGE_READ);
        // mock event
        CommandEvent event = Mockito.mock(CommandEvent.class);
        // needs to match the name (case-insensitive)
        assertEquals(expected, Resolvers.PERMISSION.findSorted(event, "read messages"));
        // needs to match the enum const name (case-insensitive)
        assertEquals(expected, Resolvers.PERMISSION.findSorted(event, "message_read"));
        // needs to match the enum const name without underscores (case-insensitive)
        assertEquals(expected, Resolvers.PERMISSION.findSorted(event, "message read"));
    }

}
