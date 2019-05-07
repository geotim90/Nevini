package de.nevini.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum Module {

    CORE("core", true, true),
    GUILD("guild", true, false),
    LEGACY("legacy", false, false);

    private final String name;
    private final boolean enabledByDefault;
    private final boolean alwaysEnabled;

}
