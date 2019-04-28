package de.nevini.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum Emote {

    OK("✅"),
    NEUTRAL("✔️"),
    WARNING("⚠️"),
    ERROR("❌"),
    DISABLED("⛔"),
    PROHIBITED("\uD83D\uDEAB");

    private final String unicode;

}
