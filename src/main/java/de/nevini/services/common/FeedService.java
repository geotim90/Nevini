package de.nevini.services.common;

import de.nevini.db.feed.FeedData;
import de.nevini.db.feed.FeedId;
import de.nevini.db.feed.FeedRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FeedService {

    private final FeedRepository feedRepository;

    public FeedService(@Autowired FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public synchronized void subscribe(@NonNull TextChannel channel, @NonNull String type) {
        FeedData data = new FeedData(
                channel.getGuild().getIdLong(),
                type,
                channel.getIdLong(),
                System.currentTimeMillis()
        );
        log.info("Save data: {}", data);
        feedRepository.save(data);
    }

    public synchronized void unsubscribe(@NonNull Guild guild, @NonNull String type) {
        FeedId id = new FeedId(guild.getIdLong(), type);
        log.info("Delete data: {}", id);
        feedRepository.deleteById(id);
    }

    public FeedData getSubscription(@NonNull Guild guild, @NonNull String type) {
        return feedRepository.findById(new FeedId(guild.getIdLong(), type)).orElse(null);
    }

    public synchronized void updateSubscription(@NonNull TextChannel channel, @NonNull String type, long uts) {
        FeedData data = new FeedData(
                channel.getGuild().getIdLong(),
                type,
                channel.getIdLong(),
                uts
        );
        log.info("Save data: {}", data);
        feedRepository.save(data);
    }
}
