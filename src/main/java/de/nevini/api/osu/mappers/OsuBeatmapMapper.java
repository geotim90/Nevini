package de.nevini.api.osu.mappers;

import de.nevini.api.osu.external.model.OsuApiBeatmap;
import de.nevini.api.osu.jpa.beatmap.*;
import de.nevini.api.osu.model.*;
import lombok.NonNull;

import static de.nevini.api.osu.mappers.OsuMapperUtils.*;

public class OsuBeatmapMapper {

    public static @NonNull OsuBeatmapDataWrapper map(@NonNull OsuApiBeatmap beatmap, OsuScore score) {
        return new OsuBeatmapDataWrapper(
                new OsuBeatmapData(
                        beatmap.getBeatmapId(),
                        beatmap.getBeatmapsetId(),
                        beatmap.getBpm(),
                        beatmap.getDifficultySize(),
                        beatmap.getDifficultyOverall(),
                        beatmap.getDifficultyApproach(),
                        beatmap.getDifficultyDrain(),
                        beatmap.getHitLength(),
                        beatmap.getTotalLength(),
                        beatmap.getVersion(),
                        beatmap.getFileMd5(),
                        beatmap.getMode(),
                        beatmap.getFavouriteCount(),
                        beatmap.getRating(),
                        beatmap.getPlayCount(),
                        beatmap.getPassCount(),
                        beatmap.getCountNormal(),
                        beatmap.getCountSlider(),
                        beatmap.getCountSpinner()
                ),
                new OsuBeatmapsetData(
                        beatmap.getBeatmapsetId(),
                        beatmap.getApproved(),
                        convertDate(beatmap.getSubmitDate()),
                        convertDate(beatmap.getApprovedDate()),
                        convertDate(beatmap.getLastUpdate()),
                        beatmap.getArtist(),
                        beatmap.getCreatorName(),
                        beatmap.getCreatorId(),
                        beatmap.getSource(),
                        beatmap.getGenre(),
                        beatmap.getLanguage(),
                        beatmap.getTitle(),
                        beatmap.getTags(),
                        beatmap.getDownloadUnavailable(),
                        beatmap.getAudioUnavailable()
                ),
                new OsuBeatmapDifficultyData(
                        beatmap.getBeatmapId(),
                        score != null ? convertMode(score.getMode()) : beatmap.getMode(),
                        score != null ? convertMods(score.getMods()) : OsuMod.NONE,
                        beatmap.getDifficultyRating(),
                        beatmap.getDifficultyAim(),
                        beatmap.getDifficultySpeed(),
                        beatmap.getMaxCombo(),
                        score != null ? score.getPp() : null
                )
        );
    }

    public static @NonNull OsuBeatmap map(@NonNull OsuBeatmapDataWrapper wrapper) {
        return new OsuBeatmap(
                convertApproved(wrapper.getBeatmapset().getApproved()),
                wrapper.getBeatmapset().getSubmitDate(),
                wrapper.getBeatmapset().getApprovedDate(),
                wrapper.getBeatmapset().getLastUpdate(),
                wrapper.getBeatmapset().getArtist(),
                wrapper.getBeatmap().getBeatmapId(),
                wrapper.getBeatmap().getBeatmapsetId(),
                wrapper.getBeatmap().getBpm(),
                wrapper.getBeatmapset().getCreatorName(),
                wrapper.getBeatmapset().getCreatorId(),
                wrapper.getDifficulty().getDifficultyRating(),
                wrapper.getDifficulty().getDifficultyAim(),
                wrapper.getDifficulty().getDifficultySpeed(),
                wrapper.getBeatmap().getDifficultySize(),
                wrapper.getBeatmap().getDifficultyOverall(),
                wrapper.getBeatmap().getDifficultyApproach(),
                wrapper.getBeatmap().getDifficultyDrain(),
                wrapper.getBeatmap().getHitLength(),
                wrapper.getBeatmapset().getSource(),
                convertGenre(wrapper.getBeatmapset().getGenre()),
                convertLanguage(wrapper.getBeatmapset().getLanguage()),
                wrapper.getBeatmapset().getTitle(),
                wrapper.getBeatmap().getTotalLength(),
                wrapper.getBeatmap().getVersion(),
                wrapper.getBeatmap().getFileMd5(),
                convertMode(wrapper.getBeatmap().getMode()),
                convertMode(wrapper.getDifficulty().getMode()),
                convertMods(wrapper.getDifficulty().getMods()),
                wrapper.getBeatmapset().getTags(),
                wrapper.getBeatmap().getFavouriteCount(),
                wrapper.getBeatmap().getRating(),
                wrapper.getBeatmap().getPlayCount(),
                wrapper.getBeatmap().getPassCount(),
                wrapper.getBeatmap().getCountNormal(),
                wrapper.getBeatmap().getCountSlider(),
                wrapper.getBeatmap().getCountSpinner(),
                wrapper.getDifficulty().getMaxCombo(),
                wrapper.getDifficulty().getMaxPp(),
                wrapper.getBeatmapset().getDownloadUnavailable(),
                wrapper.getBeatmapset().getAudioUnavailable()
        );
    }

    public static OsuBeatmap map(@NonNull OsuBeatmapViewData view) {
        return new OsuBeatmap(
                convertApproved(view.getApproved()),
                view.getSubmitDate(),
                view.getApprovedDate(),
                view.getLastUpdate(),
                view.getArtist(),
                view.getBeatmapId(),
                view.getBeatmapsetId(),
                view.getBpm(),
                view.getCreatorName(),
                view.getCreatorId(),
                view.getDifficultyRating(),
                view.getDifficultyAim(),
                view.getDifficultySpeed(),
                view.getDifficultySize(),
                view.getDifficultyOverall(),
                view.getDifficultyApproach(),
                view.getDifficultyDrain(),
                view.getHitLength(),
                view.getSource(),
                convertGenre(view.getGenre()),
                convertLanguage(view.getLanguage()),
                view.getTitle(),
                view.getTotalLength(),
                view.getVersion(),
                view.getFileMd5(),
                convertMode(view.getMode()),
                convertMode(view.getMode()),
                convertMods(view.getMods()),
                view.getTags(),
                view.getFavouriteCount(),
                view.getRating(),
                view.getPlayCount(),
                view.getPassCount(),
                view.getCountNormal(),
                view.getCountSlider(),
                view.getCountSpinner(),
                view.getMaxCombo(),
                view.getMaxPp(),
                view.getDownloadUnavailable(),
                view.getAudioUnavailable()
        );
    }

    private static OsuStatus convertApproved(Integer value) {
        if (value != null) {
            for (OsuStatus e : OsuStatus.values()) {
                if (e.getId() == value) {
                    return e;
                }
            }
        }
        return null;
    }

    private static OsuGenre convertGenre(Integer value) {
        if (value != null) {
            for (OsuGenre e : OsuGenre.values()) {
                if (e.getId() == value) {
                    return e;
                }
            }
        }
        return null;
    }

    private static OsuLanguage convertLanguage(Integer value) {
        if (value != null) {
            for (OsuLanguage e : OsuLanguage.values()) {
                if (e.getId() == value) {
                    return e;
                }
            }
        }
        return null;
    }

}
