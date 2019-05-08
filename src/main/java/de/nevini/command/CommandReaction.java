package de.nevini.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum CommandReaction {

    OK("\u2705"),
    DEFAULT_OK("\u2714"),
    DEFAULT_NOK("\u2716"),
    WARNING("\u26A0"),
    ERROR("\u274C"),
    DISABLED("\u26D4"),
    PROHIBITED("\uD83D\uDEAB"),
    DM("\u2709");

    private final String unicode;

}
