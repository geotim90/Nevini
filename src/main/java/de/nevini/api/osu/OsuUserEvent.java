package de.nevini.api.osu;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Builder
@Value
public class OsuUserEvent {

    private final String displayHtml;
    private final Integer beatmapId;
    private final Integer beatmapsetId;
    private final Date date;
    private final Integer epicFactor;

}
