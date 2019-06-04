package de.nevini.bot.command;

import de.nevini.bot.scope.Node;
import de.nevini.bot.scope.Permissions;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import net.dv8tion.jda.core.Permission;

@Value
@Builder
public class CommandDescriptor {

    /**
     * The keyword for this command (required).
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
     * The permission node for this command ({@code null} by default).
     */
    @Builder.Default
    private final Node node = null;

    /**
     * The minimum bot permissions required for this command to function.
     * By default these are the following permissions:
     * <ul>
     * <li>{@link Permission#MESSAGE_READ}</li>
     * <li>{@link Permission#MESSAGE_WRITE}</li>
     * <li>{@link Permission#MESSAGE_HISTORY}</li>
     * <li>{@link Permission#MESSAGE_ADD_REACTION}</li>
     * <li>{@link Permission#MESSAGE_EXT_EMOJI}</li>
     * <li>{@link Permission#MESSAGE_EMBED_LINKS}</li>
     * </ul>
     */
    @NonNull
    @Builder.Default
    private final Permission[] minimumBotPermissions = Permissions.BOT_EMBED;

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
    private final Permission[] minimumUserPermissions = Permissions.TALK;

    /**
     * A short description of this command (required).
     */
    @NonNull
    private final String description;

    /**
     * Details on the available command options (empty by default).
     */
    @NonNull
    @Builder.Default
    private final CommandOptionDescriptor[] options = {};

    /**
     * A more detailed description of this command.
     */
    private final String details;

}
