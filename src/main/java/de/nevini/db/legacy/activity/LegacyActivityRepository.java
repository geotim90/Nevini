package de.nevini.db.legacy.activity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyActivityRepository extends CrudRepository<LegacyActivityData, LegacyActivityId> {

}
