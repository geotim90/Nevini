package de.nevini.api.osu.model;

import de.nevini.api.osu.external.requests.OsuUserType;
import lombok.Value;

@Value
public class OsuReplay {

    private final OsuMode mode;
    private final Integer beatmapId;
    private final String user;
    private final OsuUserType userType;
    private final OsuMod[] mods;
    private final String content;
    private final String encoding;

}
