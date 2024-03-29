package de.nevini.modules.guild.activity.services;

import de.nevini.modules.admin.game.data.GameData;
import de.nevini.modules.guild.activity.cache.ActivityDataService;
import de.nevini.modules.guild.activity.jpa.ActivityData;
import de.nevini.modules.guild.activity.jpa.ActivityId;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ActivityService {

    private static final byte ACTIVITY_TYPE_ONLINE = 1;
    private static final byte ACTIVITY_TYPE_MESSAGE = 2;
    private static final byte ACTIVITY_TYPE_PLAYING = 3;
    private static final byte ACTIVITY_TYPE_AWAY = 4;

    private final ActivityDataService dataService;

    public ActivityService(@Autowired ActivityDataService dataService) {
        this.dataService = dataService;
    }

    public Long getActivityOnline(@NonNull Member member) {
        ActivityData data = dataService.get(
                new ActivityId(member.getUser().getIdLong(), ACTIVITY_TYPE_ONLINE, -1L, -1L)
        );
        ActivityData manualData = dataService.get(new ActivityId(member.getUser().getIdLong(), ACTIVITY_TYPE_ONLINE,
                -1L, member.getGuild().getIdLong()));
        if (manualData == null) {
            return data == null ? null : data.getUts();
        } else if (data == null) {
            return manualData.getUts();
        } else {
            return Math.max(data.getUts(), manualData.getUts());
        }
    }

    public void updateActivityOnline(@NonNull User user) {
        dataService.put(new ActivityData(
                user.getIdLong(), ACTIVITY_TYPE_ONLINE, -1L, -1L, System.currentTimeMillis()
        ));
    }

    public void manualActivityOnline(@NonNull Member member, @NonNull OffsetDateTime timestamp) {
        dataService.put(new ActivityData(member.getUser().getIdLong(), ACTIVITY_TYPE_ONLINE,
                member.getGuild().getIdLong(), member.getGuild().getIdLong(), timestamp.toInstant().toEpochMilli()));
    }

    public Long getActivityMessage(@NonNull Member member) {
        ActivityData data = dataService.get(new ActivityId(member.getUser().getIdLong(), ACTIVITY_TYPE_MESSAGE,
                member.getGuild().getIdLong(), -1L));
        ActivityData manualData = dataService.get(new ActivityId(member.getUser().getIdLong(), ACTIVITY_TYPE_MESSAGE,
                member.getGuild().getIdLong(), member.getGuild().getIdLong()));
        if (manualData == null) {
            return data == null ? null : data.getUts();
        } else if (data == null) {
            return manualData.getUts();
        } else {
            return Math.max(data.getUts(), manualData.getUts());
        }
    }

    public void updateActivityMessage(@NonNull Message message) {
        dataService.put(new ActivityData(
                message.getAuthor().getIdLong(),
                ACTIVITY_TYPE_MESSAGE,
                message.getGuild().getIdLong(),
                -1L,
                message.getTimeCreated().toInstant().toEpochMilli()
        ));
    }

    public void manualActivityMessage(@NonNull Member member, @NonNull OffsetDateTime timestamp) {
        dataService.put(new ActivityData(member.getUser().getIdLong(), ACTIVITY_TYPE_MESSAGE,
                member.getGuild().getIdLong(), member.getGuild().getIdLong(), timestamp.toInstant().toEpochMilli()));
    }

    public @NonNull Map<Long, Long> getActivityPlaying(@NonNull Member member) {
        Map<Long, Long> map = new HashMap<>();
        for (ActivityData e : dataService.findAllByUserAndTypeAndSourceIn(
                member.getUser().getIdLong(), ACTIVITY_TYPE_PLAYING, new long[]{-1L, member.getGuild().getIdLong()}
        )) {
            map.merge(e.getId(), e.getUts(), Math::max);
        }
        return map;
    }

    public @NonNull Map<Long, Long> getActivityPlaying(@NonNull Guild guild, @NonNull GameData game) {
        Map<Long, Long> map = new HashMap<>();
        for (ActivityData e : dataService.findAllByTypeAndIdAndSourceIn(
                ACTIVITY_TYPE_PLAYING, game.getId(), new long[]{-1L, guild.getIdLong()}
        )) {
            map.merge(e.getUser(), e.getUts(), Math::max);
        }
        return map;
    }

    public Long getActivityPlaying(@NonNull Member member, long gameId) {
        ActivityData data = dataService.get(
                new ActivityId(member.getUser().getIdLong(), ACTIVITY_TYPE_PLAYING, gameId, -1L)
        );
        ActivityData manualData = dataService.get(new ActivityId(member.getUser().getIdLong(), ACTIVITY_TYPE_PLAYING,
                gameId, member.getGuild().getIdLong()));
        if (manualData == null) {
            return data == null ? null : data.getUts();
        } else if (data == null) {
            return manualData.getUts();
        } else {
            return Math.max(data.getUts(), manualData.getUts());
        }
    }

    public void updateActivityPlaying(@NonNull User user, @NonNull GameData game) {
        dataService.put(new ActivityData(
                user.getIdLong(), ACTIVITY_TYPE_PLAYING, game.getId(), -1L, System.currentTimeMillis()
        ));
    }

    public void manualActivityPlaying(
            @NonNull Member member, @NonNull GameData game, @NonNull OffsetDateTime timestamp
    ) {
        dataService.put(new ActivityData(member.getUser().getIdLong(), ACTIVITY_TYPE_PLAYING, game.getId(),
                member.getGuild().getIdLong(), timestamp.toInstant().toEpochMilli()));
    }

    public Long getActivityAway(@NonNull Member member) {
        ActivityData data = dataService.get(new ActivityId(member.getUser().getIdLong(), ACTIVITY_TYPE_AWAY,
                member.getGuild().getIdLong(), member.getGuild().getIdLong()));
        return data == null ? null : data.getUts();
    }

    public void manualActivityAway(@NonNull Member member, @NonNull OffsetDateTime timestamp) {
        dataService.put(new ActivityData(member.getUser().getIdLong(), ACTIVITY_TYPE_AWAY,
                member.getGuild().getIdLong(), member.getGuild().getIdLong(), timestamp.toInstant().toEpochMilli()));
    }

}
