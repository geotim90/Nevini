package de.nevini.api.osu.jpa.beatmap;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OsuBeatmapRepository extends CrudRepository<OsuBeatmapData, Integer> {

}
