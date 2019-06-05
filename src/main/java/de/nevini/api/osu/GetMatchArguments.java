package de.nevini.api.osu;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class GetMatchArguments {

    @NonNull
    private final Integer matchId;

}
