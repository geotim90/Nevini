package de.nevini.jpa.inactivity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface InactivityRepository extends CrudRepository<InactivityData, InactivityId> {

    Collection<InactivityData> findAllByGuildAndType(long guild, byte type);

}
