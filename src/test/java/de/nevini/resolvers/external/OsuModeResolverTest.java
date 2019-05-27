package de.nevini.resolvers.external;

import com.oopsjpeg.osu4j.GameMode;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class OsuModeResolverTest {

    private static final OsuModeResolver resolver = new OsuModeResolver();

    @Test
    public void findCatchTheBeat() {
        ArrayList<GameMode> expected = new ArrayList<>(1);
        expected.add(GameMode.CATCH_THE_BEAT);
        // needs to match the name (case-insensitive)
        assertEquals(expected, resolver.findSorted(null, "osu!catch"));
        // needs to match the name (case-insensitive substring)
        assertEquals(expected, resolver.findSorted(null, "catch"));
        // needs to match the enum const name (case-insensitive)
        assertEquals(expected, resolver.findSorted(null, "catch_the_beat"));
        // needs to match the enum const name without underscores (case-insensitive)
        assertEquals(expected, resolver.findSorted(null, "catch the beat"));
    }

}
