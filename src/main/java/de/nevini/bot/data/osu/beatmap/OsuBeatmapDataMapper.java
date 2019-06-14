package de.nevini.bot.data.osu.beatmap;

import de.nevini.api.osu.model.*;
import de.nevini.bot.db.osu.beatmap.OsuBeatmapData;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class OsuBeatmapDataMapper {

    public static OsuBeatmap convert(OsuBeatmapData data) {
        if (data == null) return null;
        return OsuBeatmap.builder()
                .approved(convertApproved(data.getApproved()))
                .submitDate(convertDate(data.getSubmitDate()))
                .approvedDate(convertDate(data.getApprovedDate()))
                .lastUpdate(convertDate(data.getLastUpdate()))
                .artist(data.getArtist())
                .beatmapId(data.getBeatmapId())
                .beatmapsetId(data.getBeatmapsetId())
                .bpm(data.getBpm())
                .creatorName(data.getCreatorName())
                .creatorId(data.getCreatorId())
                .difficultyRating(data.getDifficultyRating())
                .difficultyAim(data.getDifficultyAim())
                .difficultySpeed(data.getDifficultySpeed())
                .difficultySize(data.getDifficultySize())
                .difficultyOverall(data.getDifficultyOverall())
                .difficultyApproach(data.getDifficultyApproach())
                .difficultyDrain(data.getDifficultyDrain())
                .hitLength(data.getHitLength())
                .source(data.getSource())
                .genre(convertGenre(data.getGenre()))
                .language(convertLanguage(data.getLanguage()))
                .title(data.getTitle())
                .totalLength(data.getTotalLength())
                .version(data.getVersion())
                .fileMd5(data.getFileMd5())
                .mode(convertMode(data.getMode()))
                .tags(convertTags(data.getTags()))
                .favouriteCount(data.getFavouriteCount())
                .rating(data.getRating())
                .playCount(data.getPlayCount())
                .passCount(data.getPassCount())
                .countNormal(data.getCountNormal())
                .countSlider(data.getCountSlider())
                .countSpinner(data.getCountSpinner())
                .maxCombo(data.getMaxCombo())
                .downloadUnavailable(data.getDownloadUnavailable())
                .audioUnavailable(data.getAudioUnavailable())
                .build();
    }

    private static OsuBeatmapApproved convertApproved(Integer value) {
        if (value == null) return null;
        for (OsuBeatmapApproved e : OsuBeatmapApproved.values()) {
            if (e.getId() == value) {
                return e;
            }
        }
        throw new IllegalArgumentException(value + " is not a valid approved id");
    }

    private static Date convertDate(Long value) {
        return value == null ? null : new Date(value);
    }

    private static OsuBeatmapGenre convertGenre(Integer value) {
        if (value == null) return null;
        for (OsuBeatmapGenre e : OsuBeatmapGenre.values()) {
            if (e.getId() == value) {
                return e;
            }
        }
        throw new IllegalArgumentException(value + " is not a valid genre id");
    }

    private static OsuBeatmapLanguage convertLanguage(Integer value) {
        if (value == null) return null;
        for (OsuBeatmapLanguage e : OsuBeatmapLanguage.values()) {
            if (e.getId() == value) {
                return e;
            }
        }
        throw new IllegalArgumentException(value + " is not a valid genre id");
    }

    private static OsuMode convertMode(Integer value) {
        if (value == null) return null;
        for (OsuMode e : OsuMode.values()) {
            if (e.getId() == value) {
                return e;
            }
        }
        throw new IllegalArgumentException(value + " is not a valid genre id");
    }

    private static String[] convertTags(String value) {
        return StringUtils.split(value, ' ');
    }

    public static OsuBeatmapData convert(OsuBeatmap beatmap) {
        if (beatmap == null) return null;
        OsuBeatmapData data = new OsuBeatmapData();
        data.setApproved(convertApproved(beatmap.getApproved()));
        data.setSubmitDate(convertDate(beatmap.getSubmitDate()));
        data.setApprovedDate(convertDate(beatmap.getApprovedDate()));
        data.setLastUpdate(convertDate(beatmap.getLastUpdate()));
        data.setArtist(beatmap.getArtist());
        data.setBeatmapId(beatmap.getBeatmapId());
        data.setBeatmapsetId(beatmap.getBeatmapsetId());
        data.setBpm(beatmap.getBpm());
        data.setCreatorName(beatmap.getCreatorName());
        data.setCreatorId(beatmap.getCreatorId());
        data.setDifficultyRating(beatmap.getDifficultyRating());
        data.setDifficultyAim(beatmap.getDifficultyAim());
        data.setDifficultySpeed(beatmap.getDifficultySpeed());
        data.setDifficultySize(beatmap.getDifficultySize());
        data.setDifficultyOverall(beatmap.getDifficultyOverall());
        data.setDifficultyApproach(beatmap.getDifficultyApproach());
        data.setDifficultyDrain(beatmap.getDifficultyDrain());
        data.setHitLength(beatmap.getHitLength());
        data.setSource(beatmap.getSource());
        data.setGenre(convertGenre(beatmap.getGenre()));
        data.setLanguage(convertLanguage(beatmap.getLanguage()));
        data.setTitle(beatmap.getTitle());
        data.setTotalLength(beatmap.getTotalLength());
        data.setVersion(beatmap.getVersion());
        data.setFileMd5(beatmap.getFileMd5());
        data.setMode(convertMode(beatmap.getMode()));
        data.setTags(convertTags(beatmap.getTags()));
        data.setFavouriteCount(beatmap.getFavouriteCount());
        data.setRating(beatmap.getRating());
        data.setPlayCount(beatmap.getPlayCount());
        data.setPassCount(beatmap.getPassCount());
        data.setCountNormal(beatmap.getCountNormal());
        data.setCountSlider(beatmap.getCountSlider());
        data.setCountSpinner(beatmap.getCountSpinner());
        data.setMaxCombo(beatmap.getMaxCombo());
        data.setDownloadUnavailable(beatmap.getDownloadUnavailable());
        data.setAudioUnavailable(beatmap.getAudioUnavailable());
        return data;
    }

    private static Integer convertApproved(OsuBeatmapApproved value) {
        return value == null ? null : value.getId();
    }

    private static Long convertDate(Date value) {
        return value == null ? null : value.getTime();
    }

    private static Integer convertGenre(OsuBeatmapGenre value) {
        return value == null ? null : value.getId();
    }

    private static Integer convertLanguage(OsuBeatmapLanguage value) {
        return value == null ? null : value.getId();
    }

    private static Integer convertMode(OsuMode value) {
        return value == null ? null : value.getId();
    }

    private static String convertTags(String[] value) {
        return StringUtils.join(value, ' ');
    }

}
