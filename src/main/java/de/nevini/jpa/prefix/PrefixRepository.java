package de.nevini.jpa.prefix;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrefixRepository extends CrudRepository<PrefixData, Long> {

}
