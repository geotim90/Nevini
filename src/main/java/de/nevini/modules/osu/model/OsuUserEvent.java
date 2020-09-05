package de.nevini.modules.osu.model;

import lombok.Value;

@Value
public class OsuUserEvent {

    String displayHtml;
    Integer beatmapId;
    Integer beatmapsetId;
    Long date;
    Integer epicFactor;

}
