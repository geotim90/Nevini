package de.nevini.command;

import de.nevini.modules.Module;
import de.nevini.modules.Node;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import net.dv8tion.jda.core.Permission;

@Value
@Builder
public class CommandDescriptor {

    /**
     * The keyword for this command (mandatory).
     */
    @NonNull
    private final String keyword;

    /**
     * Alternative keywords for this command (empty by default).
     */
    @NonNull
    @Builder.Default
    private final String[] aliases = {};

    /**
     * The children of this command (empty by default).
     */
    @NonNull
    @Builder.Default
    private final Command[] children = {};

    /**
     * Whether or not this command can only be executed by the bot owner ({@code false} by default).
     */
    @Builder.Default
    private final boolean ownerOnly = false;

    /**
     * Whether or not this command can only be executed via guild text message ({@code true} by default).
     * This option prohibits the use of this command via direct message.
     */
    @Builder.Default
    private final boolean guildOnly = true;

    /**
     * The {@link Module} that this command belongs to (mandatory).
     */
    @NonNull
    private final Module module;

    /**
     * The permission node for this command ({@code null} by default).
     */
    private final Node node;

    /**
     * The minimum bot permissions required for this command to function.
     * By default these are the following permissions:
     * <ul>
     * <li>{@link Permission#MESSAGE_READ}</li>
     * <li>{@link Permission#MESSAGE_WRITE}</li>
     * <li>{@link Permission#MESSAGE_EMBED_LINKS}</li>
     * <li>{@link Permission#MESSAGE_ADD_REACTION}</li>
     * <li>{@link Permission#MESSAGE_HISTORY}</li>
     * </ul>
     */
    @NonNull
    @Builder.Default
    private final Permission[] minimumBotPermissions = {
            Permission.MESSAGE_READ,
            Permission.MESSAGE_WRITE,
            Permission.MESSAGE_HISTORY,
            Permission.MESSAGE_EMBED_LINKS,
            Permission.MESSAGE_ADD_REACTION
    };

    /**
     * The minimum user permissions required for this command to function.
     * By default these are the following permissions:
     * <ul>
     * <li>{@link Permission#MESSAGE_READ}</li>
     * <li>{@link Permission#MESSAGE_WRITE}</li>
     * <li>{@link Permission#MESSAGE_HISTORY}</li>
     * </ul>
     */
    @NonNull
    @Builder.Default
    private final Permission[] minimumUserPermissions = {
            Permission.MESSAGE_READ,
            Permission.MESSAGE_WRITE,
            Permission.MESSAGE_HISTORY
    };

    /**
     * The default user permissions required for this command in case no permission node overrides exist (empty by default).
     */
    @NonNull
    @Builder.Default
    private final Permission[] defaultUserPermissions = {};

    /**
     * A short description of this command (mandatory).
     */
    @NonNull
    private final String description;

    /**
     * The argument syntax for this command (empty by default).
     */
    @NonNull
    @Builder.Default
    private final String syntax = "";

    /**
     * A more detailed description of this command.
     */
    private final String details;

}
