package de.nevini.db.legacy.timeout;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegacyTimeoutRepository extends CrudRepository<LegacyTimeoutData, LegacyTimeoutId> {

    List<LegacyTimeoutData> findAllByGuildAndType(long guild, byte type);

}
