package de.nevini.resolvers.external;

import com.oopsjpeg.osu4j.GameMod;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class OsuModsResolverTest {

    private final OsuModsResolver resolver = new OsuModsResolver();

    @Test
    public void findHardRock() {
        GameMod[] expected = {GameMod.HARD_ROCK};
        // needs to match the name (case-insensitive)
        assertArrayEquals(expected, resolver.findSorted(null, "hard rock").get(0));
        // needs to match the name (case-insensitive substring)
        assertArrayEquals(expected, resolver.findSorted(null, "rock").get(0));
        // needs to match the enum const name (case-insensitive)
        assertArrayEquals(expected, resolver.findSorted(null, "hard_rock").get(0));
        // needs to match the enum const name without underscores (case-insensitive)
        assertArrayEquals(expected, resolver.findSorted(null, "hard rock").get(0));
        // needs to match the short code (case-insensitive)
        assertArrayEquals(expected, resolver.findSorted(null, "HR").get(0));
    }

}
