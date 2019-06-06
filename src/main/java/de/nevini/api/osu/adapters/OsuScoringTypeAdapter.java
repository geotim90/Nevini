package de.nevini.api.osu.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import de.nevini.api.osu.model.OsuScoringType;

import java.io.IOException;

public class OsuScoringTypeAdapter extends TypeAdapter<OsuScoringType> {

    @Override
    public void write(JsonWriter out, OsuScoringType value) throws IOException {
        out.value(value == null ? null : value.getId());
    }

    @Override
    public OsuScoringType read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            int value = in.nextInt();
            for (OsuScoringType e : OsuScoringType.values()) {
                if (e.getId() == value) {
                    return e;
                }
            }
            throw new IllegalStateException(value + " is not a valid id");
        }
    }

}
