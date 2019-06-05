package de.nevini.api.osu;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Builder
@Value
public class GetBeatmapsArguments {

    private final Date since;
    private final Integer beatmapsetId;
    private final Integer beatmapId;
    private final String user;
    private final OsuUserType userType;
    private final OsuMode mode;
    private final Boolean includeConvertedBeatmaps;
    private final String beatmapHash;
    private final Integer limit;
    private final OsuMod[] mods;

}
