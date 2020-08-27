package de.nevini.modules.admin.game.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameNameMapRepository extends CrudRepository<GameNameMapData, String> {

}
