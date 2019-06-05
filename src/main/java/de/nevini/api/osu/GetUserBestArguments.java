package de.nevini.api.osu;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class GetUserBestArguments {

    @NonNull
    private final String user;
    private final OsuUserType userType;
    private final OsuMode mode;
    private final Integer limit;

}
