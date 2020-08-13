package de.nevini.api.osu.model;

import lombok.Value;

import java.util.List;

@Value
public class OsuMatch {

    Integer matchId;
    String name;
    Long startTime;
    Long endTime;
    List<OsuGame> games;

}
