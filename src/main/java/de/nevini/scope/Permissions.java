package de.nevini.scope;

import net.dv8tion.jda.api.Permission;

/**
 * Collections of commonly used permissions for Nevini.
 */
public class Permissions {

    /**
     * An empty collection.
     */
    public static final Permission[] NONE = {};

    /**
     * A collection of required permission for "talking" in text channels.
     */
    public static final Permission[] TALK = {
            Permission.MESSAGE_READ,
            Permission.MESSAGE_WRITE,
            Permission.MESSAGE_HISTORY
    };

    /**
     * A collection of required permission for "reacting" in text channels.
     * This does not include the use of external emoji.
     */
    public static final Permission[] REACT = {
            Permission.MESSAGE_READ,
            Permission.MESSAGE_HISTORY,
            Permission.MESSAGE_ADD_REACTION
    };

    /**
     * A collection of permissions typically required by Nevini.
     * This includes "talking", "reacting" and using embeds.
     */
    public static final Permission[] BOT_EMBED = sum(
            TALK,
            REACT,
            new Permission[]{Permission.MESSAGE_EMBED_LINKS}
    );

    /**
     * A collection of permissions typically required by Nevini.
     * This includes "talking", "reacting" with external emoji and using embeds.
     */
    public static final Permission[] BOT_EMBED_EXT = sum(
            BOT_EMBED,
            new Permission[]{Permission.MESSAGE_EXT_EMOJI}
    );

    /**
     * A collection of recommended permissions for Nevini.
     * This includes managing messages, "talking", "reacting" with external emoji and using embeds.
     */
    public static final Permission[] BOT_FULL = sum(
            BOT_EMBED_EXT,
            new Permission[]{Permission.MESSAGE_MANAGE}
    );

    /**
     * An empty collection.
     */
    public static final Permission[] EVERYONE = {};

    /**
     * Just {@link Permission#NICKNAME_MANAGE}.
     */
    public static final Permission[] MANAGE_NICKNAMES = {Permission.NICKNAME_MANAGE};

    /**
     * Just {@link Permission#MANAGE_ROLES}.
     */
    public static final Permission[] MANAGE_ROLES = {Permission.MANAGE_ROLES};

    /**
     * Just {@link Permission#MANAGE_SERVER}.
     */
    public static final Permission[] MANAGE_SERVER = {Permission.MANAGE_SERVER};

    /**
     * Returns a combination of all provided permission arrays.
     * The returned array will contain each {@link Permission} at most once.
     */
    public static Permission[] sum(Permission[]... permissions) {
        long bitmask = Permission.getRaw(permissions[0]);
        for (int i = 1; i < permissions.length; ++i) {
            bitmask |= Permission.getRaw(permissions[i]);
        }
        return Permission.getPermissions(bitmask).toArray(new Permission[0]);
    }

}
