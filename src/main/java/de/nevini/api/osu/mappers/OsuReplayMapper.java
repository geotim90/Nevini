package de.nevini.api.osu.mappers;

import de.nevini.api.osu.external.model.OsuApiReplay;
import de.nevini.api.osu.external.requests.OsuApiGetReplayRequest;
import de.nevini.api.osu.model.OsuReplay;
import lombok.NonNull;

import static de.nevini.api.osu.mappers.OsuMapperUtils.convertMode;
import static de.nevini.api.osu.mappers.OsuMapperUtils.convertMods;

public class OsuReplayMapper {

    public static @NonNull OsuReplay map(@NonNull OsuApiReplay replay, @NonNull OsuApiGetReplayRequest request) {
        return new OsuReplay(
                convertMode(request.getMode()),
                request.getBeatmapId(),
                request.getUser(),
                request.getUserType(),
                convertMods(request.getMods()),
                replay.getContent(),
                replay.getEncoding()
        );
    }

}
