package de.nevini.api.osu.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import de.nevini.api.osu.model.OsuBeatmapGenre;

import java.io.IOException;

public class OsuBeatmapGenreAdapter extends TypeAdapter<OsuBeatmapGenre> {

    @Override
    public void write(JsonWriter out, OsuBeatmapGenre value) throws IOException {
        out.value(value == null ? null : value.getId());
    }

    @Override
    public OsuBeatmapGenre read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            int value = in.nextInt();
            for (OsuBeatmapGenre e : OsuBeatmapGenre.values()) {
                if (e.getId() == value) {
                    return e;
                }
            }
            throw new IllegalStateException(value + " is not a valid genre id");
        }
    }

}
