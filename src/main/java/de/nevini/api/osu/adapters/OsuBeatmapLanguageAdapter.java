package de.nevini.api.osu.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import de.nevini.api.osu.model.OsuBeatmapLanguage;

import java.io.IOException;

public class OsuBeatmapLanguageAdapter extends TypeAdapter<OsuBeatmapLanguage> {

    @Override
    public void write(JsonWriter out, OsuBeatmapLanguage value) throws IOException {
        out.value(value == null ? null : value.getId());
    }

    @Override
    public OsuBeatmapLanguage read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            int value = in.nextInt();
            for (OsuBeatmapLanguage e : OsuBeatmapLanguage.values()) {
                if (e.getId() == value) {
                    return e;
                }
            }
            throw new IllegalStateException(value + " is not a valid language id");
        }
    }

}
