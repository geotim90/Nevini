package de.nevini.api.osu.mappers;

import de.nevini.api.osu.external.model.OsuApiUserEvent;
import de.nevini.api.osu.model.OsuUserEvent;
import lombok.NonNull;

import static de.nevini.api.osu.mappers.OsuMapperUtils.convertDate;

public class OsuUserEventMapper {

    public static @NonNull OsuUserEvent map(@NonNull OsuApiUserEvent event) {
        return new OsuUserEvent(
                event.getDisplayHtml(),
                event.getBeatmapId(),
                event.getBeatmapsetId(),
                convertDate(event.getDate()),
                event.getEpicFactor()
        );
    }

}
