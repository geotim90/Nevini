package de.nevini.bot.db.prefix;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrefixRepository extends CrudRepository<PrefixData, Long> {

}
