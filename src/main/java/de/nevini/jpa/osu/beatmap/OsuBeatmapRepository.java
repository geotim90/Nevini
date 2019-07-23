package de.nevini.jpa.osu.beatmap;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface OsuBeatmapRepository extends CrudRepository<OsuBeatmapData, Integer> {

    Collection<OsuBeatmapData> findAllByTitleContainsIgnoreCase(String title);

}
