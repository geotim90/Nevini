package de.nevini.api.osu.model;

import lombok.Builder;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Builder
@Value
public class OsuMatch {

    private final Integer matchId;
    private final String name;
    private final Date startTime;
    private final Date endTime;
    private final List<OsuGame> games;

}
