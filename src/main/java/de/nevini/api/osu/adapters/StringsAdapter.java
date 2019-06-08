package de.nevini.api.osu.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class StringsAdapter extends TypeAdapter<String[]> {

    @Override
    public void write(JsonWriter out, String[] value) throws IOException {
        out.value(StringUtils.join(value, ' '));
    }

    @Override
    public String[] read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            return StringUtils.split(in.nextString(), ' ');
        }
    }

}
