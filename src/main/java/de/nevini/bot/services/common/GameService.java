package de.nevini.bot.services.common;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import de.nevini.bot.db.game.GameData;
import de.nevini.bot.db.game.GameRepository;
import de.nevini.commons.util.Finder;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.RichPresence;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class GameService {

    private static final Map<Long, String> multiGameApplications;

    static {
        multiGameApplications = new ConcurrentHashMap<>();
        multiGameApplications.put(Long.parseUnsignedLong("438122941302046720"), "Xbox Live");
    }

    private final GameRepository gameRepository;
    private final Map<Long, String> cache = new ConcurrentHashMap<>();

    public GameService(@Autowired GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameData getGame(RichPresence presence) {
        long mappedId = presence.getApplicationIdLong();
        String mappedName = presence.getName();

        // attempt to resolve multi-game applications
        if (multiGameApplications.containsKey(presence.getApplicationIdLong())) {
            String multiGameApplicationName = multiGameApplications.get(presence.getApplicationIdLong());
            Collection<GameData> candidates = findGames(presence.getName());
            if (candidates.size() == 1) {
                mappedId = candidates.stream().findFirst().map(GameData::getId).orElse(presence.getApplicationIdLong());
                mappedName = candidates.stream().findFirst().map(GameData::getName).orElse(multiGameApplicationName);
            } else {
                mappedName = multiGameApplicationName;
            }
        }

        // log if game was remapped
        if (presence.getApplicationIdLong() != mappedId || !StringUtils.equals(presence.getName(), mappedName)) {
            if (presence.getApplicationIdLong() != mappedId) {
                // successful mapping
                log.debug("Remapping game '{}' ({}) to '{}' ({})", presence.getName(), presence.getApplicationId(),
                        mappedName, Long.toUnsignedString(mappedId));
            } else {
                // unsuccessful mapping (defaulted to multi-game application name)
                log.info("Remapping game '{}' ({}) to '{}' ({})", presence.getName(), presence.getApplicationId(),
                        mappedName, Long.toUnsignedString(mappedId));
            }
        }

        // update cache and database
        GameData game = new GameData(mappedId, mappedName, getIcon(presence));
        if (!StringUtils.equals(cache.put(mappedId, mappedName), mappedName)) {
            log.info("Save data: {}", game);
            gameRepository.save(game);
        }

        return game;
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
        if (FinderUtil.DISCORD_ID.matcher(query).matches()) {
            Optional<GameData> data = gameRepository.findById(Long.parseUnsignedLong(query));
            return data.map(Collections::singleton).orElse(Collections.emptySet());
        } else {
            return Finder.find(gameRepository.findAllByNameContainsIgnoreCase(query), GameData::getName, query);
        }
    }

    public String getGameName(long id) {
        return StringUtils.defaultIfEmpty(cache.computeIfAbsent(id, this::findGameName), Long.toUnsignedString(id));
    }

    private String findGameName(long id) {
        return gameRepository.findById(id).map(GameData::getName).orElse(null);
    }

}
