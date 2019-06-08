package de.nevini.api.osu.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import de.nevini.api.osu.model.OsuMod;

import java.io.IOException;
import java.util.Arrays;

public class OsuModsAdapter extends TypeAdapter<OsuMod[]> {

    @Override
    public void write(JsonWriter out, OsuMod[] value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            int sum = 0;
            for (OsuMod mod : value) {
                sum |= mod.getId();
            }
            out.value(sum);
        }
    }

    @Override
    public OsuMod[] read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            int value = in.nextInt();
            return Arrays.stream(OsuMod.values()).filter(mod -> (mod.getId() & value) > 0).toArray(OsuMod[]::new);
        }
    }

}
