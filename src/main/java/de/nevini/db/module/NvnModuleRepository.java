package de.nevini.db.module;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NvnModuleRepository extends CrudRepository<NvnModule, NvnModuleId>
{

}
