package de.nevini.jpa.osu.beatmap;

import de.nevini.api.osu.model.OsuBeatmap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface OsuBeatmapRepository extends CrudRepository<OsuBeatmapData, Integer> {

    Collection<OsuBeatmap> findAllByTitleContainsIgnoreCase(String title);

}
