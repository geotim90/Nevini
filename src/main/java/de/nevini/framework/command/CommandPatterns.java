package de.nevini.framework.command;

/**
 * Typical patterns for commands and command options options.
 */
public class CommandPatterns {

    /**
     * A regular expression pattern that matches valid keywords.
     * Keywords may contains lower case letters and select special characters.
     * To validate command keywords, please also check that the keyword does not match {@link #OPTION}.
     * This regular expression pattern does not contain any groups apart from the full match.
     */
    public static final String KEYWORD = "[a-z0-9+\\-!]{1,32}";

    /**
     * A regular expression pattern that matches valid options or flags.
     * This regular expression pattern contains the following groups: {@code ( 1 | 2 ) 3?}
     * <ul>
     * <li>matching group 1: option name</li>
     * <li>matching group 2: option short code</li>
     * <li>matching group 3: value</li>
     * </ul>
     * Note that this regular expression pattern will match anything prepended with an option flag.
     */
    public static final String OPTION = "(?:(?:--|//)(\\w+)|[-/](\\w))(\\s+(.+))?";

    /**
     * A regular expression pattern that matches valid mentions for users, members, channels and roles.
     * This regular expression patterns contains one group for the mentioned id.
     * Note that this regular expression pattern will not match mentions prepended with an option flag.
     */
    public static final String MENTION = "(?<!(?:(?:--|//)\\w+|[-/]\\w)\\s+)<(?:@[!&]?|#)\\d+>";

}
