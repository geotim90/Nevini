package de.nevini.api.osu.model;

import lombok.Value;

import java.util.List;

@Value
public class OsuGame {

    private final Integer gameId;
    private final Long startTime;
    private final Long endTime;
    private final Integer beatmapId;
    private final OsuMode mode;
    private final OsuMatchType matchType;
    private final OsuScoringType scoringType;
    private final OsuTeamType teamType;
    private final OsuMod[] mods;
    private final List<OsuGameScore> scores;

}
