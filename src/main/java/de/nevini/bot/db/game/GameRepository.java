package de.nevini.bot.db.game;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface GameRepository extends CrudRepository<GameData, Long> {

    Collection<GameData> findAllByNameContainsIgnoreCase(String name);

}