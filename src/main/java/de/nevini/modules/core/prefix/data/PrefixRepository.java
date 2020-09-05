package de.nevini.modules.core.prefix.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrefixRepository extends CrudRepository<PrefixData, Long> {

}
