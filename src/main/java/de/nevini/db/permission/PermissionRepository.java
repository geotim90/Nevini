package de.nevini.db.permission;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PermissionRepository extends CrudRepository<PermissionData, PermissionId> {

    Collection<PermissionData> findAllByGuildAndChannelAndTypeAndIdAndNodeStartingWith(
            long guild, long channel, byte type, long id, String node);

    Collection<PermissionData> findAllByGuildAndChannelAndTypeAndIdInAndNodeStartingWith(
            long guild, long channel, byte type, Collection<Long> ids, String node);

}
