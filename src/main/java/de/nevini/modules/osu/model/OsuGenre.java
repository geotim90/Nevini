package de.nevini.modules.osu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OsuGenre implements OsuEnum {

    ANY(0, "Any"),
    UNSPECIFIED(1, "Unspecified"),
    VIDEO_GAME(2, "Video Game"),
    ANIME(3, "Anime"),
    ROCK(4, "Rock"),
    POP(5, "Pop"),
    OTHER(6, "Other"),
    NOVELTY(7, "Novelty"),
    HIP_HOP(9, "Hip Hop"),
    ELECTRONIC(10, "Electronic"),
    METAL(11, "Metal"),
    CLASSICAL(12, "Classical"),
    FOLK(13, "Folk"),
    JAZZ(14, "Jazz");

    private final int id;
    private final String name;

}
