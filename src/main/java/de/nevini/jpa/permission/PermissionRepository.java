package de.nevini.jpa.permission;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PermissionRepository extends CrudRepository<PermissionData, PermissionId> {

    Collection<PermissionData> findAllByGuildAndChannelAndTypeAndIdInAndNode(
            long guild, long channel, byte type, Collection<Long> ids, String node);

    Collection<PermissionData> findAllByGuildAndTypeAndNode(long guild, byte type, String node);

    Collection<PermissionData> findAllByGuildAndNodeIn(long idLong, Collection<String> collect);

}
