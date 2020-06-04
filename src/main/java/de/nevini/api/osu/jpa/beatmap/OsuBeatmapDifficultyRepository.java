package de.nevini.api.osu.jpa.beatmap;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OsuBeatmapDifficultyRepository extends CrudRepository<OsuBeatmapDifficultyData, OsuBeatmapDifficultyId> {

    void deleteAllByBeatmapId(Integer beatmapId);

}
