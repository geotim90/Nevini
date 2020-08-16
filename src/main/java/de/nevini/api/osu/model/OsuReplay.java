package de.nevini.api.osu.model;

import de.nevini.api.osu.external.requests.OsuUserType;
import lombok.Value;

@Value
public class OsuReplay {

    OsuMode mode;
    Integer beatmapId;
    String user;
    OsuUserType userType;
    OsuMod[] mods;
    String content;
    String encoding;

}
