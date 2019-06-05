package de.nevini.api.osu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OsuMode {

    STANDARD(0, "osu!"),
    TAIKO(1, "osu!taiko"),
    CATCH_THE_BEAT(2, "osu!catch"),
    MANIA(3, "osu!mania");

    private final int mode;
    private final String name;

}
