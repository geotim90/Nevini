package de.nevini.jpa.activity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ActivityRepository extends CrudRepository<ActivityData, ActivityId> {

    Collection<ActivityData> findAllByUserAndTypeAndSourceIn(long user, byte type, long[] source);

    Collection<ActivityData> findAllByTypeAndIdAndSourceIn(byte type, long id, long[] source);

}
