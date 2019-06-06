package de.nevini.api.osu.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import de.nevini.api.osu.model.OsuMatchType;

import java.io.IOException;

public class OsuMatchTypeAdapter extends TypeAdapter<OsuMatchType> {

    @Override
    public void write(JsonWriter out, OsuMatchType value) throws IOException {
        out.value(value == null ? null : value.getId());
    }

    @Override
    public OsuMatchType read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            int value = in.nextInt();
            for (OsuMatchType e : OsuMatchType.values()) {
                if (e.getId() == value) {
                    return e;
                }
            }
            throw new IllegalStateException(value + " is not a valid match type id");
        }
    }

}
