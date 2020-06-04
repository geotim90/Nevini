package de.nevini.api.osu.jpa.score;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OsuScoreRepository extends CrudRepository<OsuScoreData, OsuScoreId> {

    void deleteAllByBeatmapId(Integer beatmapId);

}
