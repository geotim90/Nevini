package de.nevini.command;

public class CommandPatterns {

    public static final String KEYWORD = "[a-z0-9+\\-_.:!]{1,32}";

    /**
     * {@code ( 1 | 2 ) 3?}
     * <ul>
     * <li>matching group 1: option name</li>
     * <li>matching group 2: option short code</li>
     * <li>matching group 3: value</li>
     * </ul>
     */
    public static final String OPTION = "(?:(?:--|//)(\\w+)|[-/](\\w))(\\s+(.+))?";

    public static final String MENTION = "(?<!(?:(?:--|//)\\w+|[-/]\\w)\\s+)<(?:@[!&]?|#)\\d+>";

}
