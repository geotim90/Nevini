package de.nevini.jpa.game;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameNameMapRepository extends CrudRepository<GameNameMapData, String> {

}
