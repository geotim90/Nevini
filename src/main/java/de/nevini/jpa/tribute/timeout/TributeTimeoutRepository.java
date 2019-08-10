package de.nevini.jpa.tribute.timeout;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TributeTimeoutRepository extends CrudRepository<TributeTimeoutData, Long> {
}
