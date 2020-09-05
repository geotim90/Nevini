package de.nevini.modules.osu.mappers;

import de.nevini.modules.osu.api.model.OsuApiUserEvent;
import de.nevini.modules.osu.model.OsuUserEvent;
import lombok.NonNull;

import static de.nevini.modules.osu.mappers.OsuMapperUtils.convertDate;

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
