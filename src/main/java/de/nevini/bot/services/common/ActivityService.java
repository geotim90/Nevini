package de.nevini.bot.services.common;

import de.nevini.bot.db.activity.ActivityData;
import de.nevini.bot.db.activity.ActivityId;
import de.nevini.bot.db.activity.ActivityRepository;
import de.nevini.bot.db.game.GameData;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.RichPresence;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActivityService {

    private static final byte ACTIVITY_TYPE_ONLINE = 1;
    private static final byte ACTIVITY_TYPE_MESSAGE = 2;
    private static final byte ACTIVITY_TYPE_PLAYING = 3;

    private final ActivityRepository activityRepository;

    public ActivityService(@Autowired ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public long getActivityOnline(User user) {
        Optional<ActivityData> data = activityRepository.findById(
                new ActivityId(user.getIdLong(), ACTIVITY_TYPE_ONLINE, user.getIdLong())
        );
        return data.isPresent() ? data.get().getUts() : 0;
    }

    public synchronized void updateActivityOnline(User user) {
        ActivityData data = new ActivityData(
                user.getIdLong(), ACTIVITY_TYPE_ONLINE, user.getIdLong(), System.currentTimeMillis()
        );
        log.debug("Save data: {}", data);
        activityRepository.save(data);
    }

    public long getActivityMessage(Member member) {
        Optional<ActivityData> data = activityRepository.findById(
                new ActivityId(member.getUser().getIdLong(), ACTIVITY_TYPE_MESSAGE, member.getGuild().getIdLong())
        );
        return data.isPresent() ? data.get().getUts() : 0;
    }

    public synchronized void updateActivityMessage(Message message) {
        if (message.getGuild() != null) {
            ActivityData data = new ActivityData(
                    message.getAuthor().getIdLong(),
                    ACTIVITY_TYPE_MESSAGE,
                    message.getGuild().getIdLong(),
                    message.getCreationTime().toInstant().toEpochMilli()
            );
            log.debug("Save data: {}", data);
            activityRepository.save(data);
        }
    }

    public Map<Long, Long> getActivityPlaying(User user) {
        Collection<ActivityData> data = activityRepository.findAllByUserAndType(
                user.getIdLong(), ACTIVITY_TYPE_PLAYING
        );
        return data.stream().collect(Collectors.toMap(ActivityData::getId, ActivityData::getUts));
    }

    public Map<Long, Long> getActivityPlaying(GameData game) {
        Collection<ActivityData> data = activityRepository.findAllByTypeAndId(ACTIVITY_TYPE_PLAYING, game.getId());
        return data.stream().collect(Collectors.toMap(ActivityData::getUser, ActivityData::getUts));
    }

    public Long getActivityPlaying(User user, GameData game) {
        Optional<ActivityData> data = activityRepository.findById(
                new ActivityId(user.getIdLong(), ACTIVITY_TYPE_PLAYING, game.getId())
        );
        return data.map(ActivityData::getUts).orElse(null);
    }

    public synchronized void updateActivityPlaying(User user, RichPresence presence) {
        ActivityData data = new ActivityData(
                user.getIdLong(), ACTIVITY_TYPE_PLAYING, presence.getApplicationIdLong(), System.currentTimeMillis()
        );
        log.debug("Save data: {}", data);
        activityRepository.save(data);
    }
}
