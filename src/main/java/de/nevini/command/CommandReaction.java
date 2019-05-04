package de.nevini.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum CommandReaction {

    OK("✅"),
    NEUTRAL("✔"),
    WARNING("⚠️"),
    ERROR("❌"),
    DISABLED("⛔"),
    PROHIBITED("\uD83D\uDEAB");

    private final String unicode;

}
