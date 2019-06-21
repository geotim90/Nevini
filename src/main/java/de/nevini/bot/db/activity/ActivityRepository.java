package de.nevini.bot.db.activity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ActivityRepository extends CrudRepository<ActivityData, ActivityId> {

    Collection<ActivityData> findAllByUserAndType(long user, byte type);

    Collection<ActivityData> findAllByTypeAndIdAndSourceIn(byte type, long id, long[] source);

}
