package de.nevini.jpa.module;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends CrudRepository<ModuleData, ModuleId> {

}
