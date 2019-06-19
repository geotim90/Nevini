package de.nevini.api.wfs.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class WfsNceFloatAdapter extends TypeAdapter<Float> {

    @Override
    public void write(JsonWriter out, Float value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }

    @Override
    public Float read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            String value = in.nextString();
            if (value.isEmpty()) {
                return null;
            } else if (value.startsWith("nce: ")) {
                return Float.parseFloat(value.substring(5));
            } else {
                return Float.parseFloat(value);
            }
        }
    }

}
