package de.nevini.modules.osu.data;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OsuBeatmapViewRepository extends CrudRepository<OsuBeatmapViewData, Integer>, JpaSpecificationExecutor<OsuBeatmapViewData> {

}
