package de.nevini.bot.db.autorole;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AutoRoleRepository extends CrudRepository<AutoRoleData, AutoRoleId> {

    Collection<AutoRoleData> findAllByGuild(long guild);

}
