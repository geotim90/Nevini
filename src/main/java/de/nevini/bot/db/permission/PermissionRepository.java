package de.nevini.bot.db.permission;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PermissionRepository extends CrudRepository<PermissionData, PermissionId> {

    Collection<PermissionData> findAllByGuildAndChannelAndTypeAndIdInAndNode(
            long guild, long channel, byte type, Collection<Long> ids, String node);

}
