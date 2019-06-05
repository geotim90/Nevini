package de.nevini.api.osu;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class GetScoresArguments {

    @NonNull
    private final Integer beatmapId;
    private final String user;
    private final OsuUserType userType;
    private final String userName;
    private final OsuMode mode;
    private final OsuMod[] mods;
    private final Integer limit;

}
