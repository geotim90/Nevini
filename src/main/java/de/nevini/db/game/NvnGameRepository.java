package de.nevini.db.game;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NvnGameRepository extends CrudRepository<NvnGame, Long>
{

}
