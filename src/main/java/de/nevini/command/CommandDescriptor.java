package de.nevini.command;

import de.nevini.modules.Module;
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
     * The minimum bot permissions required for this command to function (empty by default).
     */
    @NonNull
    @Builder.Default
    private final Permission[] minimumBotPermissions = {};

    /**
     * The minimum user permissions required for this command to function (empty by default).
     */
    @NonNull
    @Builder.Default
    private final Permission[] minimumUserPermissions = {};

    /**
     * The permission node for this command ({@code null} by default).
     */
    private final String node;

    /**
     * The default user permissions required for this command to function ({@link Permission#MANAGE_SERVER} by default).
     */
    @NonNull
    @Builder.Default
    private final Permission[] defaultUserPermissions = {Permission.MANAGE_SERVER};

    /**
     * The help description for this command (mandatory).
     */
    @NonNull
    private final String description;

    /**
     * The argument syntax for this command (empty by default).
     */
    @NonNull
    @Builder.Default
    private final String syntax = "";

    public void validate() {
        if (!keyword.matches("[a-z0-9-]{1,32}")) {
            throw new IllegalStateException("Keyword does not match regex.");
        }
        for (String alias : aliases) {
            if (!alias.matches("[a-z0-9-]{1,32}")) {
                throw new IllegalStateException("Alias does not match regex.");
            }
        }
        if (node != null && !node.matches("[a-z0-9.-]{1,32}")) {
            throw new IllegalStateException("Node does not match regex.");
        }
        if (description.isEmpty()) {
            throw new IllegalStateException("Description is empty.");
        }
    }

}
