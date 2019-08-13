package de.nevini.services.common;

import de.nevini.jpa.game.GameData;
import de.nevini.jpa.inactivity.InactivityData;
import de.nevini.jpa.inactivity.InactivityId;
import de.nevini.jpa.inactivity.InactivityRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InactivityService {

    private static final byte ACTIVITY_TYPE_ONLINE = 1;
    private static final byte ACTIVITY_TYPE_MESSAGE = 2;
    private static final byte ACTIVITY_TYPE_PLAYING = 3;

    private final InactivityRepository repository;

    public InactivityService(@Autowired InactivityRepository repository) {
        this.repository = repository;
    }

    public void setOnlineThreshold(@NonNull Guild guild, int days) {
        InactivityData data = new InactivityData(guild.getIdLong(), ACTIVITY_TYPE_ONLINE, -1L, days);
        log.info("Save data: {}", data);
        repository.save(data);
    }

    public Integer getOnlineThreshold(@NonNull Guild guild) {
        return repository.findById(new InactivityId(guild.getIdLong(), ACTIVITY_TYPE_ONLINE, -1L))
                .map(InactivityData::getDays).orElse(null);
    }

    @Transactional
    public void removeOnlineThreshold(@NonNull Guild guild) {
        InactivityId id = new InactivityId(guild.getIdLong(), ACTIVITY_TYPE_ONLINE, -1L);
        if (repository.existsById(id)) {
            log.info("Delete data: {}", id);
            repository.deleteById(id);
        }
    }

    public void setMessageThreshold(@NonNull Guild guild, int days) {
        InactivityData data = new InactivityData(guild.getIdLong(), ACTIVITY_TYPE_MESSAGE, -1L, days);
        log.info("Save data: {}", data);
        repository.save(data);
    }

    public Integer getMessageThreshold(@NonNull Guild guild) {
        return repository.findById(new InactivityId(guild.getIdLong(), ACTIVITY_TYPE_MESSAGE, -1L))
                .map(InactivityData::getDays).orElse(null);
    }

    @Transactional
    public void removeMessageThreshold(@NonNull Guild guild) {
        InactivityId id = new InactivityId(guild.getIdLong(), ACTIVITY_TYPE_MESSAGE, -1L);
        if (repository.existsById(id)) {
            log.info("Delete data: {}", id);
            repository.deleteById(id);
        }
    }

    public void setPlayingThreshold(@NonNull Guild guild, @NonNull GameData game, int days) {
        InactivityData data = new InactivityData(guild.getIdLong(), ACTIVITY_TYPE_PLAYING, game.getId(), days);
        log.info("Save data: {}", data);
        repository.save(data);
    }

    public Integer getPlayingThreshold(@NonNull Guild guild, @NonNull GameData game) {
        return repository.findById(new InactivityId(guild.getIdLong(), ACTIVITY_TYPE_PLAYING, game.getId()))
                .map(InactivityData::getDays).orElse(null);
    }

    public Map<Long, Integer> getPlayingThresholds(@NonNull Guild guild) {
        return repository.findAllByGuildAndType(guild.getIdLong(), ACTIVITY_TYPE_PLAYING).stream()
                .collect(Collectors.toMap(InactivityData::getId, InactivityData::getDays));
    }

    @Transactional
    public void removePlayingThreshold(@NonNull Guild guild, @NonNull GameData game) {
        InactivityId id = new InactivityId(guild.getIdLong(), ACTIVITY_TYPE_PLAYING, game.getId());
        if (repository.existsById(id)) {
            log.info("Delete data: {}", id);
            repository.deleteById(id);
        }
    }
}
