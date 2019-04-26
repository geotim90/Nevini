package de.nevini.services;

import de.nevini.db.game.GameData;
import de.nevini.db.game.GameRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.RichPresence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(@Autowired GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void putGame(RichPresence game) {
        GameData data = new GameData(game.getApplicationIdLong(), game.getName());
        log.info("Save data: {}", data);
        gameRepository.save(data);
    }

}
