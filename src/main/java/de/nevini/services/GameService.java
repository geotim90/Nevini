package de.nevini.services;

import de.nevini.db.game.GameData;
import de.nevini.db.game.GameRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.RichPresence;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final Map<Long, String> cache = new ConcurrentHashMap<>();

    public GameService(@Autowired GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public String getGameName(long id) {
        return StringUtils.defaultIfEmpty(cache.computeIfAbsent(id, this::findGameName), Long.toUnsignedString(id));
    }

    private String findGameName(long id) {
        return gameRepository.findById(id).map(GameData::getName).orElse(null);
    }

    public synchronized void setGameName(RichPresence game) {
        if (!StringUtils.equals(cache.put(game.getApplicationIdLong(), game.getName()), game.getName())) {
            GameData data = new GameData(game.getApplicationIdLong(), game.getName(), getIcon(game));
            log.info("Save data: {}", data);
            gameRepository.save(data);
        }
    }

    private String getIcon(RichPresence game) {
        RichPresence.Image large = game.getLargeImage();
        if (large != null) {
            return large.getUrl();
        }
        RichPresence.Image small = game.getSmallImage();
        if (small != null) {
            return small.getUrl();
        }
        return null;
    }

    public Collection<GameData> findGames(String query) {
        try {
            return gameRepository.findAllByIdOrNameContainsIgnoreCase(Long.parseLong(query), query);
        } catch (NumberFormatException e) {
            return gameRepository.findAllByNameContainsIgnoreCase(query);
        }
    }

}
