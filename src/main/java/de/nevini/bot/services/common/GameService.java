package de.nevini.bot.services.common;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import de.nevini.bot.data.game.GameDataService;
import de.nevini.bot.db.game.GameData;
import de.nevini.commons.util.Finder;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.RichPresence;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class GameService {

    private static final Map<Long, String> multiGameApplications;

    static {
        multiGameApplications = new ConcurrentHashMap<>();
        multiGameApplications.put(Long.parseUnsignedLong("438122941302046720"), "Xbox Live");
    }

    private final GameDataService gameDataService;

    public GameService(@Autowired GameDataService gameDataService) {
        this.gameDataService = gameDataService;
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
        gameDataService.put(game);

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
            GameData data = gameDataService.get(Long.parseUnsignedLong(query));
            return data == null ? Collections.emptySet() : Collections.singleton(data);
        } else {
            return Finder.find(gameDataService.findAllByNameContainsIgnoreCase(query), GameData::getName, query);
        }
    }

    public String getGameName(long id) {
        GameData game = gameDataService.get(id);
        return game == null ? Long.toUnsignedString(id) : game.getName();
    }

}
