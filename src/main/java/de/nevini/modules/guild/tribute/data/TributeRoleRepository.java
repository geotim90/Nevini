package de.nevini.modules.guild.tribute.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TributeRoleRepository extends CrudRepository<TributeRoleData, Long> {
}
