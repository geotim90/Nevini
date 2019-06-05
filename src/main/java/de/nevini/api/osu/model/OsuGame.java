package de.nevini.api.osu.model;

import lombok.Builder;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Builder
@Value
public class OsuGame {

    private final Integer gameId;
    private final Date startTime;
    private final Date endTime;
    private final Integer beatmapId;
    private final OsuMode playMode;
    private final OsuMatchType matchType;
    private final OsuScoringType scoringType;
    private final OsuTeamType teamType;
    private final OsuMod[] mods;
    private final List<OsuGameScore> scores;

}
