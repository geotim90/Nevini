package de.nevini.bot.services.common;

import de.nevini.bot.data.activity.ActivityDataService;
import de.nevini.bot.db.activity.ActivityData;
import de.nevini.bot.db.activity.ActivityId;
import de.nevini.bot.db.game.GameData;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    private static final byte ACTIVITY_TYPE_ONLINE = 1;
    private static final byte ACTIVITY_TYPE_MESSAGE = 2;
    private static final byte ACTIVITY_TYPE_PLAYING = 3;

    private final ActivityDataService dataService;

    public ActivityService(@Autowired ActivityDataService dataService) {
        this.dataService = dataService;
    }

    public Long getActivityOnline(@NonNull User user) {
        ActivityData data = dataService.get(
                new ActivityId(user.getIdLong(), ACTIVITY_TYPE_ONLINE, user.getIdLong())
        );
        return data == null ? null : data.getUts();
    }

    public void updateActivityOnline(@NonNull User user) {
        dataService.put(new ActivityData(
                user.getIdLong(), ACTIVITY_TYPE_ONLINE, user.getIdLong(), System.currentTimeMillis()
        ));
    }

    public Long getActivityMessage(@NonNull Member member) {
        ActivityData data = dataService.get(
                new ActivityId(member.getUser().getIdLong(), ACTIVITY_TYPE_MESSAGE, member.getGuild().getIdLong())
        );
        return data == null ? null : data.getUts();
    }

    public void updateActivityMessage(@NonNull Message message) {
        dataService.put(new ActivityData(
                message.getAuthor().getIdLong(),
                ACTIVITY_TYPE_MESSAGE,
                message.getGuild().getIdLong(),
                message.getCreationTime().toInstant().toEpochMilli()
        ));
    }

    public @NonNull Map<Long, Long> getActivityPlaying(@NonNull User user) {
        Collection<ActivityData> data = dataService.findAllByUserAndType(
                user.getIdLong(), ACTIVITY_TYPE_PLAYING
        );
        return data.stream().collect(Collectors.toMap(ActivityData::getId, ActivityData::getUts));
    }

    public @NonNull Map<Long, Long> getActivityPlaying(@NonNull GameData game) {
        Collection<ActivityData> data = dataService.findAllByTypeAndId(ACTIVITY_TYPE_PLAYING, game.getId());
        return data.stream().collect(Collectors.toMap(ActivityData::getUser, ActivityData::getUts));
    }

    public Long getActivityPlaying(@NonNull User user, @NonNull GameData game) {
        ActivityData data = dataService.get(
                new ActivityId(user.getIdLong(), ACTIVITY_TYPE_PLAYING, game.getId())
        );
        return data == null ? null : data.getUts();
    }

    public void updateActivityPlaying(@NonNull User user, @NonNull GameData game) {
        dataService.put(new ActivityData(
                user.getIdLong(), ACTIVITY_TYPE_PLAYING, game.getId(), System.currentTimeMillis()
        ));
    }

}
