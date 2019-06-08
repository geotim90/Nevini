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

import java.util.List;

@Slf4j
@Service
public class FeedService {

    private final FeedRepository feedRepository;

    public FeedService(@Autowired FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public synchronized void subscribe(@NonNull TextChannel channel, @NonNull Feed feed) {
        FeedData data = new FeedData(
                channel.getGuild().getIdLong(),
                feed.getType(),
                channel.getIdLong(),
                System.currentTimeMillis()
        );
        log.info("Save data: {}", data);
        feedRepository.save(data);
    }

    public synchronized void unsubscribe(@NonNull Guild guild, @NonNull Feed feed) {
        FeedId id = new FeedId(guild.getIdLong(), feed.getType());
        log.info("Delete data: {}", id);
        feedRepository.deleteById(id);
    }

    public FeedData getSubscription(@NonNull Guild guild, @NonNull Feed feed) {
        return feedRepository.findById(new FeedId(guild.getIdLong(), feed.getType())).orElse(null);
    }

    public List<FeedData> getSubscriptions(@NonNull Guild guild) {
        return feedRepository.findAllByGuild(guild.getIdLong());
    }

    public List<FeedData> getSubscriptions(@NonNull TextChannel channel) {
        return feedRepository.findAllByGuildAndChannel(channel.getGuild().getIdLong(), channel.getIdLong());
    }

    public synchronized void updateSubscription(@NonNull TextChannel channel, @NonNull Feed feed, long uts) {
        FeedData data = new FeedData(
                channel.getGuild().getIdLong(),
                feed.getType(),
                channel.getIdLong(),
                uts
        );
        log.info("Save data: {}", data);
        feedRepository.save(data);
    }
}
