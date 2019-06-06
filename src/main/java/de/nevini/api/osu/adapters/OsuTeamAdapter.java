package de.nevini.api.osu.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import de.nevini.api.osu.model.OsuTeam;

import java.io.IOException;

public class OsuTeamAdapter extends TypeAdapter<OsuTeam> {

    @Override
    public void write(JsonWriter out, OsuTeam value) throws IOException {
        out.value(value == null ? null : value.getId());
    }

    @Override
    public OsuTeam read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            int value = in.nextInt();
            for (OsuTeam e : OsuTeam.values()) {
                if (e.getId() == value) {
                    return e;
                }
            }
            throw new IllegalStateException(value + " is not a valid id");
        }
    }

}
