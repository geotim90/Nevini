package de.nevini.api.osu.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import de.nevini.api.osu.model.OsuRank;

import java.io.IOException;

public class OsuRankAdapter extends TypeAdapter<OsuRank> {

    @Override
    public void write(JsonWriter out, OsuRank value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.getId());
        }
    }

    @Override
    public OsuRank read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            String value = in.nextString();
            if ("0".equals(value)) {
                return null;
            }
            for (OsuRank e : OsuRank.values()) {
                if (e.getId().equals(value)) {
                    return e;
                }
            }
            throw new IllegalStateException(value + " is not a valid rank id");
        }
    }

}
