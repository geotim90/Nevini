package de.nevini.api.osu.model;

import lombok.Value;

@Value
public class OsuUserEvent {

    private final String displayHtml;
    private final Integer beatmapId;
    private final Integer beatmapsetId;
    private final Long date;
    private final Integer epicFactor;

}
