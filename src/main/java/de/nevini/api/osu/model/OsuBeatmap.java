package de.nevini.api.osu.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Builder
@Value
public class OsuBeatmap {

    @SerializedName("approved")
    private final OsuBeatmapApproved approved;

    @SerializedName("submit_date")
    private final Date submitDate;

    @SerializedName("approved_date")
    private final Date approvedDate;

    @SerializedName("last_update")
    private final Date lastUpdate;

    @SerializedName("artist")
    private final String artist;

    @SerializedName("beatmap_id")
    private final Integer beatmapId;

    @SerializedName("beatmapset_id")
    private final Integer beatmapsetId;

    @SerializedName("bpm")
    private final Float bpm;

    @SerializedName("creator")
    private final String creatorName;

    @SerializedName("creator_id")
    private final Integer creatorId;

    @SerializedName("difficultyrating")
    private final Float difficultyRating;

    @SerializedName("diff_aim")
    private final Float difficultyAim;

    @SerializedName("diff_speed")
    private final Float difficultySpeed;

    @SerializedName("diff_size")
    private final Float difficultySize;

    @SerializedName("diff_overall")
    private final Float difficultyOverall;

    @SerializedName("diff_approach")
    private final Float difficultyApproach;

    @SerializedName("diff_drain")
    private final Float difficultyDrain;

    @SerializedName("hit_length")
    private final Integer hitLength;

    @SerializedName("source")
    private final String source;

    @SerializedName("genre_id")
    private final OsuBeatmapGenre genre;

    @SerializedName("language_id")
    private final OsuBeatmapLanguage language;

    @SerializedName("title")
    private final String title;

    @SerializedName("total_length")
    private final Integer totalLength;

    @SerializedName("version")
    private final String version;

    @SerializedName("file_md5")
    private final String fileMd5;

    @SerializedName("mode")
    private final OsuMode mode;

    @SerializedName("tags")
    private final String[] tags;

    @SerializedName("favourite_count")
    private final Integer favouriteCount;

    @SerializedName("rating")
    private final Float rating;

    @SerializedName("playcount")
    private final Integer playCount;

    @SerializedName("passcount")
    private final Integer passCount;

    @SerializedName("count_normal")
    private final Integer countNormal;

    @SerializedName("count_slider")
    private final Integer countSlider;

    @SerializedName("count_spinner")
    private final Integer countSpinner;

    @SerializedName("max_combo")
    private final Integer maxCombo;

    @SerializedName("download_unavailable")
    private final Boolean downloadUnavailable;

    @SerializedName("audio_unavailable")
    private final Boolean audioUnavailable;

}
