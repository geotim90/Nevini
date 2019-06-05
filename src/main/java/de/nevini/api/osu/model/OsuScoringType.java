package de.nevini.api.osu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OsuScoringType {

    SCORE(0, "Score"),
    ACCURACY(1, "Accuracy"),
    COMBO(2, "Combo"),
    SCORE_V2(3, "Score v2");

    private final int scoringType;
    private final String name;

}
