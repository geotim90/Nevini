package de.nevini.jpa.tribute.role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TributeRoleRepository extends CrudRepository<TributeRoleData, Long> {
}
