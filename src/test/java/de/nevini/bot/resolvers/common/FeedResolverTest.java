package de.nevini.bot.resolvers.common;

import de.nevini.bot.scope.Feed;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class FeedResolverTest {

    @Test
    public void findOsuEvents() {
        ArrayList<Feed> expected = new ArrayList<>(1);
        expected.add(Feed.OSU_EVENTS);
        // needs to match the type (case-insensitive)
        assertEquals(expected, Resolvers.FEED.findSorted(null, "osu.events"));
        // needs to match the name (case-insensitive)
        assertEquals(expected, Resolvers.FEED.findSorted(null, "osu! events"));
        // needs to match the enum const name (case-insensitive)
        assertEquals(expected, Resolvers.FEED.findSorted(null, "osu_events"));
        // needs to match the enum const name without underscores (case-insensitive)
        assertEquals(expected, Resolvers.FEED.findSorted(null, "osu events"));
    }

}
