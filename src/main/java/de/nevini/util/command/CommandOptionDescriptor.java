package de.nevini.util.command;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Describes a command option or flag, e.g. {@code [--user] [<user>]}.
 */
@Value
@Builder
public class CommandOptionDescriptor {

    /**
     * The full syntax for this command option (required).
     */
    @NonNull
    String syntax;

    /**
     * A short description of this command option (required).
     */
    @NonNull
    String description;

    /**
     * The keyword for this command option (required).
     */
    @NonNull
    String keyword;

    /**
     * Alternative keywords for this command option (empty by default).
     */
    @NonNull
    @Builder.Default
    String[] aliases = {};

}
