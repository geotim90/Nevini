package de.nevini.api.osu.external.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.Date;

@Value
public class OsuApiBeatmap {

    @SerializedName("approved")
    Integer approved;

    @SerializedName("submit_date")
    Date submitDate;

    @SerializedName("approved_date")
    Date approvedDate;

    @SerializedName("last_update")
    Date lastUpdate;

    @SerializedName("artist")
    String artist;

    @SerializedName("beatmap_id")
    Integer beatmapId;

    @SerializedName("beatmapset_id")
    Integer beatmapsetId;

    @SerializedName("bpm")
    Double bpm;

    @SerializedName("creator")
    String creatorName;

    @SerializedName("creator_id")
    Integer creatorId;

    @SerializedName("difficultyrating")
    Double difficultyRating;

    @SerializedName("diff_aim")
    Double difficultyAim;

    @SerializedName("diff_speed")
    Double difficultySpeed;

    @SerializedName("diff_size")
    Double difficultySize;

    @SerializedName("diff_overall")
    Double difficultyOverall;

    @SerializedName("diff_approach")
    Double difficultyApproach;

    @SerializedName("diff_drain")
    Double difficultyDrain;

    @SerializedName("hit_length")
    Integer hitLength;

    @SerializedName("source")
    String source;

    @SerializedName("genre_id")
    Integer genre;

    @SerializedName("language_id")
    Integer language;

    @SerializedName("title")
    String title;

    @SerializedName("total_length")
    Integer totalLength;

    @SerializedName("version")
    String version;

    @SerializedName("file_md5")
    String fileMd5;

    @SerializedName("mode")
    Integer mode;

    @SerializedName("tags")
    String tags;

    @SerializedName("favourite_count")
    Integer favouriteCount;

    @SerializedName("rating")
    Double rating;

    @SerializedName("playcount")
    Integer playCount;

    @SerializedName("passcount")
    Integer passCount;

    @SerializedName("count_normal")
    Integer countNormal;

    @SerializedName("count_slider")
    Integer countSlider;

    @SerializedName("count_spinner")
    Integer countSpinner;

    @SerializedName("max_combo")
    Integer maxCombo;

    @SerializedName("download_unavailable")
    Boolean downloadUnavailable;

    @SerializedName("audio_unavailable")
    Boolean audioUnavailable;

}
