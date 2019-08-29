package de.nevini.api.osu.model;

import lombok.Value;

import java.util.List;

@Value
public class OsuMatch {

    private final Integer matchId;
    private final String name;
    private final Long startTime;
    private final Long endTime;
    private final List<OsuGame> games;

}
