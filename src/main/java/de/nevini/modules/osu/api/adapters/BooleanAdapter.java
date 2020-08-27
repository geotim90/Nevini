package de.nevini.modules.osu.api.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class BooleanAdapter extends TypeAdapter<Boolean> {

    @Override
    public void write(JsonWriter out, Boolean value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value ? 1 : 0);
        }
    }

    @Override
    public Boolean read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            int value = in.nextInt();
            switch (value) {
                case 1:
                    return Boolean.TRUE;
                case 0:
                    return Boolean.FALSE;
                default:
                    throw new IllegalStateException(value + " is not a valid boolean value!");
            }
        }
    }

}
