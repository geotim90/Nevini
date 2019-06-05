package de.nevini.api.osu.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class OsuReplay {

    private final String content;
    private final String encoding;

}
