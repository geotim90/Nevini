package de.nevini.modules.admin.game.services;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import de.nevini.modules.admin.game.cache.GameDataService;
import de.nevini.modules.admin.game.data.*;
import de.nevini.util.Finder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.RichPresence;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
public class GameService {

    private final GameDataService gameDataService;
    private final GameNameMapRepository gameNameMapRepository;
    private final GameIdMapRepository gameIdMapRepository;

    public GameService(
            @Autowired GameDataService gameDataService,
            @Autowired GameNameMapRepository gameNameMapRepository,
            @Autowired GameIdMapRepository gameIdMapRepository
    ) {
        this.gameDataService = gameDataService;
        this.gameNameMapRepository = gameNameMapRepository;
        this.gameIdMapRepository = gameIdMapRepository;
    }

    public GameData getGame(@NonNull RichPresence presence) {
        long mappedId = presence.getApplicationIdLong();
        String mappedName = presence.getName();
        String mappedIcon = getIcon(presence);

        // resolve name mappings
        GameNameMapData nameMapping = gameNameMapRepository.findById(mappedName).orElse(null);
        if (nameMapping != null) {
            mappedId = nameMapping.getId();
        }

        // resolve id mappings
        GameIdMapData idMapping = gameIdMapRepository.findById(mappedId).orElse(null);
        if (idMapping != null) {
            if (idMapping.getReject()) return null;
            mappedName = ObjectUtils.defaultIfNull(idMapping.getName(), mappedName);
            mappedIcon = ObjectUtils.defaultIfNull(idMapping.getIcon(), mappedIcon);
            // attempt to resolve multi-game applications
            if (idMapping.getMulti()) {
                Collection<GameData> candidates = findGames(presence.getName());
                if (candidates.size() == 1) {
                    mappedId = candidates.stream().findFirst().map(GameData::getId).orElse(mappedId);
                    mappedName = candidates.stream().findFirst().map(GameData::getName).orElse(mappedName);
                } else if (!candidates.isEmpty()) {
                    log.info("Ambiguous game '{}' ({})", presence.getName(), presence.getApplicationId());
                }
            }
        }

        if (mappedId != 0) {
            // log if game was remapped
            if (presence.getApplicationIdLong() != mappedId) {
                log.debug("Remapping game '{}' ({}) to '{}' ({})", presence.getName(), presence.getApplicationId(),
                        mappedName, Long.toUnsignedString(mappedId));
            }

            // update cache and database
            GameData game = new GameData(mappedId, mappedName, mappedIcon);
            gameDataService.put(game);
            return game;
        } else {
            return null;
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

    public void mapIdByName(long id, String name) {
        GameNameMapData data = new GameNameMapData(name, id);
        log.info("Save data: {}", data);
        gameNameMapRepository.save(data);
    }

    public void mapNameById(String name, long id) {
        GameIdMapData data = gameIdMapRepository.findById(id).orElse(new GameIdMapData(id, null, null, false, false));
        data.setName(StringUtils.defaultIfBlank(name, null));
        log.info("Save data: {}", data);
        gameIdMapRepository.save(data);
    }

    public void mapIconById(String icon, long id) {
        GameIdMapData data = gameIdMapRepository.findById(id).orElse(new GameIdMapData(id, null, null, false, false));
        data.setIcon(StringUtils.defaultIfBlank(icon, null));
        log.info("Save data: {}", data);
        gameIdMapRepository.save(data);
    }

    public void mapMultiById(boolean multi, long id) {
        GameIdMapData data = gameIdMapRepository.findById(id).orElse(new GameIdMapData(id, null, null, false, false));
        data.setMulti(multi);
        log.info("Save data: {}", data);
        gameIdMapRepository.save(data);
    }

    public void mapRejectById(boolean reject, long id) {
        GameIdMapData data = gameIdMapRepository.findById(id).orElse(new GameIdMapData(id, null, null, false, false));
        data.setReject(reject);
        log.info("Save data: {}", data);
        gameIdMapRepository.save(data);
    }

}
