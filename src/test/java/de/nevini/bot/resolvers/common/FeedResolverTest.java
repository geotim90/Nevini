package de.nevini.bot.resolvers.common;

import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.scope.Feed;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class FeedResolverTest {

    @Test
    public void findOsuEvents() {
        // mock event
        CommandEvent event = Mockito.mock(CommandEvent.class);
        Mockito.when(event.isBotOwner()).thenReturn(false);
        // set expected result
        ArrayList<Feed> expected = new ArrayList<>(1);
        expected.add(Feed.OSU_EVENTS);
        // needs to match the type (case-insensitive)
        assertEquals(expected, Resolvers.FEED.findSorted(event, "osu.events"));
        // needs to match the name (case-insensitive)
        assertEquals(expected, Resolvers.FEED.findSorted(event, "osu! events"));
        // needs to match the enum const name (case-insensitive)
        assertEquals(expected, Resolvers.FEED.findSorted(event, "osu_events"));
        // needs to match the enum const name without underscores (case-insensitive)
        assertEquals(expected, Resolvers.FEED.findSorted(event, "osu events"));
    }

}
