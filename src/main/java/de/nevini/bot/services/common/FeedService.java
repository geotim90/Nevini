package de.nevini.bot.services.common;

import de.nevini.bot.db.feed.FeedData;
import de.nevini.bot.db.feed.FeedId;
import de.nevini.bot.db.feed.FeedRepository;
import de.nevini.bot.scope.Feed;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

@Slf4j
@Service
public class FeedService {

    private final FeedRepository feedRepository;

    public FeedService(@Autowired FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    /**
     * Creates the main entry for the specified feed in the database.
     *
     * @param feed    the {@link Feed} to subscribe to
     * @param channel the {@link TextChannel} to point the feed to
     */
    public synchronized void subscribe(@NonNull Feed feed, @NonNull TextChannel channel) {
        FeedData data = new FeedData(
                channel.getGuild().getIdLong(),
                feed.getType(),
                -1L,
                channel.getIdLong(),
                System.currentTimeMillis()
        );
        log.debug("Save data: {}", data);
        feedRepository.save(data);
    }

    /**
     * Removed all entries for the specified feed in the database.
     *
     * @param feed  the {@link Feed} to unsubscribe from
     * @param guild the relevant {@link Guild}
     */
    @Transactional
    public synchronized void unsubscribe(@NonNull Feed feed, @NonNull Guild guild) {
        FeedId id = new FeedId(guild.getIdLong(), feed.getType(), -1L);
        log.debug("Delete data: {}", id);
        feedRepository.deleteById(id);
        feedRepository.deleteAllByGuildAndType(guild.getIdLong(), feed.getType());
    }

    /**
     * Returns the main entry for the specified feed.
     * Returns {@code null} if none exists.
     *
     * @param feed  the {@link Feed} to look up
     * @param guild the relevant {@link Guild}
     */
    public FeedData getSubscription(@NonNull Feed feed, @NonNull Guild guild) {
        return getSubscription(feed, guild, -1L);
    }

    /**
     * Returns a specific entry for the specified feed.
     * Returns {@code null} if none exists and no main entry exists.
     *
     * @param feed  the {@link Feed} to look up
     * @param guild the relevant {@link Guild}
     * @param id    the relevant id ({@code -1L} = main entry)
     */
    public FeedData getSubscription(@NonNull Feed feed, @NonNull Guild guild, long id) {
        // try to find specific entry
        return feedRepository.findById(new FeedId(guild.getIdLong(), feed.getType(), id)).orElse(
                // try to find main entry
                feedRepository.findById(new FeedId(guild.getIdLong(), feed.getType(), -1L)).map(
                        // create a "new" specific entry from the main entry
                        data -> new FeedData(data.getGuild(), data.getType(), id, data.getChannel(), data.getUts())
                ).orElse(null)
        );
    }

    /**
     * Returns the main entry for all feeds of the specified type.
     *
     * @param feed the {@link Feed} to look up
     */
    public Collection<FeedData> getSubscription(@NonNull Feed feed) {
        return feedRepository.findAllByTypeAndId(feed.getType(), -1L);
    }

    /**
     * Returns the main entry for all feeds of the specified guild.
     *
     * @param guild the relevant {@link Guild}
     */
    public Collection<FeedData> getSubscriptions(@NonNull Guild guild) {
        return feedRepository.findAllByGuildAndId(guild.getIdLong(), -1L);
    }

    /**
     * Returns the main entries for all feeds pointing to the specified channel.
     *
     * @param channel the relevant {@link TextChannel}
     */
    public Collection<FeedData> getSubscriptions(@NonNull TextChannel channel) {
        return feedRepository.findAllByGuildAndIdAndChannel(channel.getGuild().getIdLong(), -1L, channel.getIdLong());
    }

    /**
     * Updates the main entry and the entry of the specified feed and id.
     *
     * @param feed    the relevant {@link Feed}
     * @param id      the relevant id ({@code -1L} = main entry)
     * @param channel the relevant {@link TextChannel}
     * @param uts     the new update timestamp
     */
    public synchronized void updateSubscription(
            @NonNull Feed feed, @NonNull Long id, @NonNull TextChannel channel, long uts
    ) {
        // update main feed entry
        if (id != -1) {
            FeedData data = new FeedData(
                    channel.getGuild().getIdLong(),
                    feed.getType(),
                    -1L,
                    channel.getIdLong(),
                    uts
            );
            log.debug("Save data: {}", data);
            feedRepository.save(data);
        }

        // update feed entry for id
        FeedData data = new FeedData(
                channel.getGuild().getIdLong(),
                feed.getType(),
                id,
                channel.getIdLong(),
                uts
        );
        log.debug("Save data: {}", data);
        feedRepository.save(data);
    }

}
