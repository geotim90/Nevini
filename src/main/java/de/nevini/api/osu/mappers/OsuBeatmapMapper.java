package de.nevini.api.osu.mappers;

import de.nevini.api.osu.external.model.OsuApiBeatmap;
import de.nevini.api.osu.jpa.beatmap.OsuBeatmapData;
import de.nevini.api.osu.model.*;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import static de.nevini.api.osu.mappers.OsuMapperUtils.convertDate;
import static de.nevini.api.osu.mappers.OsuMapperUtils.convertMode;

public class OsuBeatmapMapper {

    public static @NonNull OsuBeatmapData map(@NonNull OsuApiBeatmap beatmap, OsuScore score) {
        return new OsuBeatmapData(
                beatmap.getApproved(),
                convertDate(beatmap.getSubmitDate()),
                convertDate(beatmap.getApprovedDate()),
                convertDate(beatmap.getLastUpdate()),
                beatmap.getArtist(),
                beatmap.getBeatmapId(),
                beatmap.getBeatmapsetId(),
                beatmap.getBpm(),
                beatmap.getCreatorName(),
                beatmap.getCreatorId(),
                beatmap.getDifficultyRating(),
                beatmap.getDifficultyAim(),
                beatmap.getDifficultySpeed(),
                beatmap.getDifficultySize(),
                beatmap.getDifficultyOverall(),
                beatmap.getDifficultyApproach(),
                beatmap.getDifficultyDrain(),
                beatmap.getHitLength(),
                beatmap.getSource(),
                beatmap.getGenre(),
                beatmap.getLanguage(),
                beatmap.getTitle(),
                beatmap.getTotalLength(),
                beatmap.getVersion(),
                beatmap.getFileMd5(),
                beatmap.getMode(),
                beatmap.getTags(),
                beatmap.getFavouriteCount(),
                beatmap.getRating(),
                beatmap.getPlayCount(),
                beatmap.getPassCount(),
                beatmap.getCountNormal(),
                beatmap.getCountSlider(),
                beatmap.getCountSpinner(),
                beatmap.getMaxCombo(),
                score == null ? null : score.getPp(),
                beatmap.getDownloadUnavailable(),
                beatmap.getAudioUnavailable()
        );
    }

    public static @NonNull OsuBeatmap map(@NonNull OsuBeatmapData beatmap) {
        return new OsuBeatmap(
                convertApproved(beatmap.getApproved()),
                beatmap.getSubmitDate(),
                beatmap.getApprovedDate(),
                beatmap.getLastUpdate(),
                beatmap.getArtist(),
                beatmap.getBeatmapId(),
                beatmap.getBeatmapsetId(),
                beatmap.getBpm(),
                beatmap.getCreatorName(),
                beatmap.getCreatorId(),
                beatmap.getDifficultyRating(),
                beatmap.getDifficultyAim(),
                beatmap.getDifficultySpeed(),
                beatmap.getDifficultySize(),
                beatmap.getDifficultyOverall(),
                beatmap.getDifficultyApproach(),
                beatmap.getDifficultyDrain(),
                beatmap.getHitLength(),
                beatmap.getSource(),
                convertGenre(beatmap.getGenre()),
                convertLanguage(beatmap.getLanguage()),
                beatmap.getTitle(),
                beatmap.getTotalLength(),
                beatmap.getVersion(),
                beatmap.getFileMd5(),
                convertMode(beatmap.getMode()),
                convertTags(beatmap.getTags()),
                beatmap.getFavouriteCount(),
                beatmap.getRating(),
                beatmap.getPlayCount(),
                beatmap.getPassCount(),
                beatmap.getCountNormal(),
                beatmap.getCountSlider(),
                beatmap.getCountSpinner(),
                beatmap.getMaxCombo(),
                beatmap.getMaxPp(),
                beatmap.getDownloadUnavailable(),
                beatmap.getAudioUnavailable()
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

    private static String[] convertTags(String value) {
        return StringUtils.split(value, ' ');
    }

}
