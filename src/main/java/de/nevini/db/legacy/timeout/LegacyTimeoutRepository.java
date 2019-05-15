package de.nevini.db.legacy.timeout;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyTimeoutRepository extends CrudRepository<LegacyTimeoutData, LegacyTimeoutId> {

}
