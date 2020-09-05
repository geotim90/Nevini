package de.nevini.modules.osu.model;

import lombok.Value;

import java.util.List;

@Value
public class OsuGame {

    Integer gameId;
    Long startTime;
    Long endTime;
    Integer beatmapId;
    OsuMode mode;
    OsuMatchType matchType;
    OsuScoringType scoringType;
    OsuTeamType teamType;
    OsuMod[] mods;
    List<OsuGameScore> scores;

}
