package de.nevini.api.osu.jpa.beatmap;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface OsuBeatmapRepository extends CrudRepository<OsuBeatmapData, Integer> {

    Collection<OsuBeatmapData> findAllByTitleContainsIgnoreCase(String title);

}
