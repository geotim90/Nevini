package de.nevini.api.osu;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import okhttp3.OkHttpClient;

import java.util.List;

@AllArgsConstructor
public class OsuApi {

    @NonNull
    private final OkHttpClient httpClient;

    @NonNull
    private final String apiToken;

    public List<OsuBeatmap> getBeatmaps(GetBeatmapsArguments arguments) {
        return null; // TODO
    }

    public OsuUser getUser(GetUserArguments arguments) {
        return null; // TODO
    }

    public List<OsuScore> getScores(GetScoresArguments arguments) {
        return null; // TODO
    }

    public List<OsuUserBest> getUserBest(GetUserBestArguments arguments) {
        return null; // TODO
    }

    public List<OsuUserRecent> getUserRecent(GetUserRecentArguments arguments) {
        return null; // TODO
    }

    public OsuMatch getMatch(GetMatchArguments arguments) {
        return null; // TODO
    }

}
