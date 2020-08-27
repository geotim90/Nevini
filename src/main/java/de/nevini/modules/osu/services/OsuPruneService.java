package de.nevini.modules.osu.services;

import de.nevini.modules.osu.data.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class OsuPruneService {

    private final OsuBeatmapRepository beatmapRepository;
    private final OsuBeatmapsetRepository beatmapsetRepository;
    private final OsuBeatmapDifficultyRepository difficultyRepository;
    private final OsuScoreRepository scoreRepository;

    public OsuPruneService(
            @Autowired OsuBeatmapRepository beatmapRepository,
            @Autowired OsuBeatmapsetRepository beatmapsetRepository,
            @Autowired OsuBeatmapDifficultyRepository difficultyRepository,
            @Autowired OsuScoreRepository scoreRepository
    ) {
        this.beatmapRepository = beatmapRepository;
        this.beatmapsetRepository = beatmapsetRepository;
        this.difficultyRepository = difficultyRepository;
        this.scoreRepository = scoreRepository;
    }

    @Transactional
    public synchronized void pruneBeatmap(Integer beatmapId) {
        Optional<OsuBeatmapData> data = beatmapRepository.findById(beatmapId);
        if (data.isPresent()) {
            log.debug("Pruning beatmapId {} from database", beatmapId);
            scoreRepository.deleteAllByBeatmapId(beatmapId);
            difficultyRepository.deleteAllByBeatmapId(beatmapId);
            beatmapRepository.deleteById(beatmapId);
        }
    }

    @Transactional
    public synchronized void pruneBeatmapset(Integer beatmapsetId) {
        Optional<OsuBeatmapsetData> data = beatmapsetRepository.findById(beatmapsetId);
        if (data.isPresent()) {
            log.debug("Pruning beatmapsetId {} from database", beatmapsetId);
            beatmapRepository.findAllByBeatmapsetId(beatmapsetId).forEach(e -> pruneBeatmap(e.getBeatmapId()));
            beatmapsetRepository.deleteById(beatmapsetId);
        }
    }

}
