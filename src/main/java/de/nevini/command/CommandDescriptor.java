package de.nevini.command;

import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import net.dv8tion.jda.api.Permission;

@Value
@Builder
public class CommandDescriptor {

    /**
     * The keyword for this command (required).
     */
    @NonNull
    String keyword;

    /**
     * Alternative keywords for this command (empty by default).
     */
    @NonNull
    @Builder.Default
    String[] aliases = {};

    /**
     * The children of this command (empty by default).
     */
    @NonNull
    @Builder.Default
    Command[] children = {};

    /**
     * Whether or not this command can only be executed by the bot owner ({@code false} by default).
     */
    @Builder.Default
    boolean ownerOnly = false;

    /**
     * Whether or not this command can only be executed via guild text message ({@code true} by default).
     * This option prohibits the use of this command via direct message.
     */
    @Builder.Default
    boolean guildOnly = true;

    /**
     * The permission node for this command ({@code null} by default).
     */
    @Builder.Default
    Node node = null;

    /**
     * The minimum bot permissions required for this command to function.
     * By default these are {@link Permissions#BOT_EMBED}.
     */
    @NonNull
    @Builder.Default
    Permission[] minimumBotPermissions = Permissions.BOT_EMBED;

    /**
     * A short description of this command (required).
     */
    @NonNull
    String description;

    /**
     * Details on the available command options (empty by default).
     */
    @NonNull
    @Builder.Default
    CommandOptionDescriptor[] options = {};

    /**
     * A more detailed description of this command.
     */
    String details;

}
