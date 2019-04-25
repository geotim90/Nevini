package de.nevini.db.permission;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NvnPermissionRepository extends CrudRepository<NvnPermission, NvnPermissionId>
{

}
