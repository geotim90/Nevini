package de.nevini.api.osu;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Builder
@Value
public class OsuBeatmap {

    private final OsuBeatmapApproved approved;
    private final Date submitDate;
    private final Date approvedDate;
    private final Date lastUpdate;
    private final String artist;
    private final Integer beatmapId;
    private final Integer beatmapsetId;
    private final Integer bpm;
    private final String creatorName;
    private final Integer creatorId;
    private final Float difficultyRating;
    private final Float difficultyAim;
    private final Float difficultySpeed;
    private final Float difficultySize;
    private final Float difficultyOverall;
    private final Float difficultyApproach;
    private final Float difficultyDrain;
    private final Integer hitLength;
    private final String source;
    private final OsuBeatmapGenre genre;
    private final OsuBeatmapLanguage language;
    private final String title;
    private final Integer totalLength;
    private final String version;
    private final String fileMd5;
    private final OsuMode mode;
    private final String[] tags;
    private final Integer favouriteCount;
    private final Float rating;
    private final Integer playCount;
    private final Integer passCount;
    private final Integer maxCombo;
    private final Boolean downloadUnavailable;
    private final Boolean audioUnavailable;

}
