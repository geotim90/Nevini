package de.nevini.api.osu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OsuLanguage {

    ANY(0, "Any"),
    OTHER(1, "Other"),
    ENGLISH(2, "English"),
    JAPANESE(3, "Japanese"),
    CHINESE(4, "Chinese"),
    INSTRUMENTAL(5, "Instrumental"),
    KOREAN(6, "Korean"),
    FRENCH(7, "French"),
    GERMAN(8, "German"),
    SWEDISH(9, "Swedish"),
    SPANISH(10, "Spanish"),
    ITALIAN(11, "Italian");

    private final int id;
    private final String name;

}
