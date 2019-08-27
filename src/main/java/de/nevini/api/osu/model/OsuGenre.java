package de.nevini.api.osu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OsuGenre {

    ANY(0, "Any"),
    UNSPECIFIED(1, "Unspecified"),
    VIDEO_GAME(2, "Video Game"),
    ANIME(3, "Anime"),
    ROCK(4, "Rock"),
    POP(5, "Pop"),
    OTHER(6, "Other"),
    NOVELTY(7, "Novelty"),
    HIP_HOP(9, "Hip Hop"),
    ELECTRONIC(10, "Electronic");

    private final int id;
    private final String name;

}
