package de.nevini.modules.osu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OsuLanguage implements OsuEnum {

    ANY(0, "Any"),
    UNSPECIFIED(1, "Unspecified"),
    ENGLISH(2, "English (EN)"),
    JAPANESE(3, "Japanese (JA)"),
    CHINESE(4, "Chinese (ZH)"),
    INSTRUMENTAL(5, "Instrumental"),
    KOREAN(6, "Korean (KO)"),
    FRENCH(7, "French (FR)"),
    GERMAN(8, "German (DE)"),
    SWEDISH(9, "Swedish (SV)"),
    SPANISH(10, "Spanish (ES)"),
    ITALIAN(11, "Italian (IT)"),
    RUSSIAN(12, "Russian (RU)"),
    POLISH(13, "Polish (PL)"),
    OTHER(14, "Other");

    private final int id;
    private final String name;

}
