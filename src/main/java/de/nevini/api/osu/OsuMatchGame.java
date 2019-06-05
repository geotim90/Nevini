package de.nevini.api.osu;

import lombok.Builder;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Builder
@Value
public class OsuMatchGame {

    private final Integer gameId;
    private final Date startTime;
    private final Date endTime;
    private final Integer beatmapId;
    private final OsuMode playMode;
    private final OsuMatchGameMatchType matchType;
    private final OsuMatchGameScoringType scoringType;
    private final OsuMatchGameTeamType teamType;
    private final OsuMod[] mods;
    private final List<OsuMatchGameScore> scores;

}
