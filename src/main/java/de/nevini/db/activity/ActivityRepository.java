package de.nevini.db.activity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ActivityRepository extends CrudRepository<ActivityData, ActivityId> {

    Collection<ActivityData> findAllByUserAndType(long user, byte type);

}
