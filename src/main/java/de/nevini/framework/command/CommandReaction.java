package de.nevini.framework.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * An {@link Enum} containing unicode emoji for standard reactions.
 */
@AllArgsConstructor
@Getter
@ToString
public enum CommandReaction {

    /**
     * For positive responses.
     * Rendered as a green tick in Discord.
     */
    OK("\u2705"),

    /**
     * For positive responses that had no effect.
     * Rendered as a black tick in Discord.
     */
    DEFAULT_OK("\u2611"),

    /**
     * For negative responses that had no effect.
     * Rendered as a black cross in Discord.
     */
    DEFAULT_NOK("\u2716"),

    /**
     * For "unknown" responses.
     * Rendered as a red question mark in Discord.
     */
    UNKNOWN("\u2754"),

    /**
     * For warning responses (error states that can be easily corrected by the user).
     * Rendered as a yellow warning triangle in Discord.
     */
    WARNING("\u26A0"),

    /**
     * For error responses (error states that cannot be easily corrected by the user).
     * Rendered as a red cross in Discord.
     */
    ERROR("\u274C"),

    /**
     * For when something is disabled by configuration.
     * Rendered as a red no-entry sign in Discord.
     */
    DISABLED("\u26D4"),

    /**
     * For when something is prohibited by configuration.
     * Rendered as a red cancel/abort sign in Discord.
     */
    PROHIBITED("\uD83D\uDEAB"),

    /**
     * For when a response was sent via direct message.
     * Rendered as a white envelope in Discord.
     */
    DM("\u2709");

    /**
     * The unicode for this emoji.
     */
    private final String unicode;

}
