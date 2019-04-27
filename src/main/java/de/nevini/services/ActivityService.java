package de.nevini.services;

import de.nevini.db.activity.ActivityData;
import de.nevini.db.activity.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.RichPresence;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void updateActivityOnline(User user) {
        ActivityData data = new ActivityData(user.getIdLong(), ACTIVITY_TYPE_ONLINE, user.getIdLong(), System.currentTimeMillis());
        log.info("Save data: {}", data);
        activityRepository.save(data);
    }

    public void updateActivityMessage(Message message) {
        if (message.getGuild() != null) {
            ActivityData data = new ActivityData(message.getAuthor().getIdLong(), ACTIVITY_TYPE_MESSAGE, message.getGuild().getIdLong(), message.getCreationTime().toInstant().toEpochMilli());
            log.info("Save data: {}", data);
            activityRepository.save(data);
        }
    }

    public void updateActivityPlaying(User user, RichPresence presence) {
        ActivityData data = new ActivityData(user.getIdLong(), ACTIVITY_TYPE_PLAYING, presence.getApplicationIdLong(), System.currentTimeMillis());
        log.info("Save data: {}", data);
        activityRepository.save(data);
    }

}
