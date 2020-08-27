package de.nevini.modules.osu.model;

import de.nevini.modules.osu.api.requests.OsuUserType;
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
