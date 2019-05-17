package de.nevini.services;

import de.nevini.db.game.GameData;
import de.nevini.db.legacy.timeout.LegacyTimeoutData;
import de.nevini.db.legacy.timeout.LegacyTimeoutId;
import de.nevini.db.legacy.timeout.LegacyTimeoutRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LegacyTimeoutService {

    private static final byte LAST_ONLINE = 1;
    private static final byte LAST_MESSAGE = 2;
    private static final byte LAST_PLAYED = 3;
    private static final byte CONTRIBUTION = 4;

    private final LegacyTimeoutRepository legacyTimeoutRepository;

    public LegacyTimeoutService(@Autowired @NonNull LegacyTimeoutRepository legacyTimeoutRepository) {
        this.legacyTimeoutRepository = legacyTimeoutRepository;
    }

    public Optional<Long> getLastOnlineTimeout(@NonNull Guild guild) {
        return legacyTimeoutRepository.findById(new LegacyTimeoutId(guild.getIdLong(), LAST_ONLINE, -1L))
                .map(LegacyTimeoutData::getValue);
    }

    public synchronized void setLastOnlineTimeout(@NonNull Guild guild, long millis) {
        LegacyTimeoutData data = new LegacyTimeoutData(guild.getIdLong(), LAST_ONLINE, -1L, millis);
        log.info("Save data: {}", data);
        legacyTimeoutRepository.save(data);
    }

    public synchronized void removeLastOnlineTimeout(@NonNull Guild guild) {
        LegacyTimeoutId id = new LegacyTimeoutId(guild.getIdLong(), LAST_ONLINE, -1L);
        log.info("Delete data: {}", id);
        legacyTimeoutRepository.deleteById(id);
    }

    public Optional<Long> getLastMessageTimeout(@NonNull Guild guild) {
        return legacyTimeoutRepository.findById(new LegacyTimeoutId(guild.getIdLong(), LAST_MESSAGE, -1L))
                .map(LegacyTimeoutData::getValue);
    }

    public synchronized void setLastMessageTimeout(@NonNull Guild guild, long millis) {
        LegacyTimeoutData data = new LegacyTimeoutData(guild.getIdLong(), LAST_MESSAGE, -1L, millis);
        log.info("Save data: {}", data);
        legacyTimeoutRepository.save(data);
    }

    public synchronized void removeLastMessageTimeout(@NonNull Guild guild) {
        LegacyTimeoutId id = new LegacyTimeoutId(guild.getIdLong(), LAST_MESSAGE, -1L);
        log.info("Delete data: {}", id);
        legacyTimeoutRepository.deleteById(id);
    }

    public Optional<Long> getLastPlayedTimeout(@NonNull Guild guild, @NonNull GameData game) {
        return legacyTimeoutRepository.findById(new LegacyTimeoutId(guild.getIdLong(), LAST_PLAYED, game.getId()))
                .map(LegacyTimeoutData::getValue);
    }

    public List<LegacyTimeoutData> getLastPlayedTimeouts(@NonNull Guild guild) {
        return legacyTimeoutRepository.findAllByGuildAndType(guild.getIdLong(), LAST_PLAYED);
    }

    public synchronized void setLastPlayedTimeout(@NonNull Guild guild, @NonNull GameData game, long millis) {
        LegacyTimeoutData data = new LegacyTimeoutData(guild.getIdLong(), LAST_PLAYED, game.getId(), millis);
        log.info("Save data: {}", data);
        legacyTimeoutRepository.save(data);
    }

    public synchronized void removeLastPlayedTimeout(@NonNull Guild guild, @NonNull GameData game) {
        LegacyTimeoutId id = new LegacyTimeoutId(guild.getIdLong(), LAST_PLAYED, game.getId());
        log.info("Delete data: {}", id);
        legacyTimeoutRepository.deleteById(id);
    }

    public Optional<Long> getContributionTimeout(@NonNull Guild guild) {
        return legacyTimeoutRepository.findById(new LegacyTimeoutId(guild.getIdLong(), CONTRIBUTION, -1L))
                .map(LegacyTimeoutData::getValue);
    }

    public synchronized void setContributionTimeout(@NonNull Guild guild, long millis) {
        LegacyTimeoutData data = new LegacyTimeoutData(guild.getIdLong(), CONTRIBUTION, -1L, millis);
        log.info("Save data: {}", data);
        legacyTimeoutRepository.save(data);
    }

    public synchronized void removeContributionTimeout(@NonNull Guild guild) {
        LegacyTimeoutId id = new LegacyTimeoutId(guild.getIdLong(), CONTRIBUTION, -1L);
        log.info("Delete data: {}", id);
        legacyTimeoutRepository.deleteById(id);
    }

}
