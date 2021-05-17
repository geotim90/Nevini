package de.nevini.modules.warframe.api.wfs.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class WfsIntegerAdapter extends TypeAdapter<Integer> {

    @Override
    public void write(JsonWriter out, Integer value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }

    @Override
    public Integer read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            String value = in.nextString();
            if (value.isEmpty() || "???".equals(value)) {
                return null;
            } else if ("Infinite".equals(value)) {
                return Integer.MAX_VALUE;
            } else {
                return Integer.parseInt(value);
            }
        }
    }

}
