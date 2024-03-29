package de.nevini.modules.osu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OsuRank {

    SSH("XH"),
    SS("X"),
    SH("SH"),
    S("S"),
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    F("F");

    private final String id;

}
