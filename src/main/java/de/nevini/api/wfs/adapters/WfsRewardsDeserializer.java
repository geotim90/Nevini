package de.nevini.api.wfs.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import de.nevini.api.wfs.model.drops.WfsReward;
import de.nevini.api.wfs.model.drops.WfsRewards;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class WfsRewardsDeserializer implements JsonDeserializer<WfsRewards> {

    @Override
    public WfsRewards deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonNull()) {
            return null;
        } else if (json.isJsonArray()) {
            TypeToken<List<WfsReward>> typeToken = new TypeToken<List<WfsReward>>() {
            };
            return WfsRewards.builder().rewards(
                    context.deserialize(json, typeToken.getType())
            ).build();
        } else {
            TypeToken<Map<String, List<WfsReward>>> typeToken = new TypeToken<Map<String, List<WfsReward>>>() {
            };
            return WfsRewards.builder().rotationRewards(
                    context.deserialize(json, typeToken.getType())
            ).build();
        }
    }

}
