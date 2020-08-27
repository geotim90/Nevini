package de.nevini.modules.osu.resolvers;

import de.nevini.core.command.CommandEvent;
import de.nevini.modules.osu.model.OsuMode;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class OsuModeResolverTest {

    private static final OsuModeResolver resolver = new OsuModeResolver();

    @Test
    public void findCatchTheBeat() {
        ArrayList<OsuMode> expected = new ArrayList<>(1);
        expected.add(OsuMode.CATCH_THE_BEAT);
        // mock event
        CommandEvent event = Mockito.mock(CommandEvent.class);
        // needs to match the name (case-insensitive)
        assertEquals(expected, resolver.findSorted(event, "osu!catch"));
        // needs to match the name (case-insensitive substring)
        assertEquals(expected, resolver.findSorted(event, "c"));
        assertEquals(expected, resolver.findSorted(event, "catch"));
        // needs to match the enum const name (case-insensitive)
        assertEquals(expected, resolver.findSorted(event, "catch_the_beat"));
        // needs to match the enum const name without underscores (case-insensitive)
        assertEquals(expected, resolver.findSorted(event, "catch the beat"));
        assertEquals(expected, resolver.findSorted(event, "catchthebeat"));
        assertEquals(expected, resolver.findSorted(event, "ctb"));
    }

}
