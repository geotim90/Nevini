package de.nevini.command;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class CommandOptionDescriptor {

    /**
     * The full syntax for this command option (required).
     */
    @NonNull
    private final String syntax;

    /**
     * A short description of this command option (required).
     */
    @NonNull
    private final String description;

    /**
     * The keyword for this command option (required).
     */
    @NonNull
    private final String keyword;

    /**
     * Alternative keywords for this command option (empty by default).
     */
    @NonNull
    @Builder.Default
    private final String[] aliases = {};

}
