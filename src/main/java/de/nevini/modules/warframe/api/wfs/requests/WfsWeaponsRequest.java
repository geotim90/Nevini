package de.nevini.modules.warframe.api.wfs.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.core.api.ApiResponse;
import de.nevini.modules.warframe.api.wfs.model.weapons.WfsWeapon;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

@Builder
@Value
public class WfsWeaponsRequest implements WfsApiRequest<List<WfsWeapon>> {

    @Override
    public @NonNull String getEndpoint() {
        return "https://api.warframestat.us/weapons";
    }

    @Override
    public @NonNull ApiResponse<List<WfsWeapon>> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    List<WfsWeapon> parseStream(Reader reader) {
        Type type = new TypeToken<List<WfsWeapon>>() {
        }.getType();
        return WfsJson.getGson().fromJson(reader, type);
    }

}
