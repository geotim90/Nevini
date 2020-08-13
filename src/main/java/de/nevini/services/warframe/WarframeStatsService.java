package de.nevini.services.warframe;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.WarframeStatsApi;
import de.nevini.api.wfs.model.drops.WfsDrops;
import de.nevini.api.wfs.model.rivens.WfsRiven;
import de.nevini.api.wfs.model.weapons.WfsWeapon;
import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.api.wfs.requests.WfsDropsRequest;
import de.nevini.api.wfs.requests.WfsRivensRequest;
import de.nevini.api.wfs.requests.WfsWeaponsRequest;
import de.nevini.api.wfs.requests.WfsWorldStateRequest;
import de.nevini.locators.Locatable;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class WarframeStatsService implements Locatable {

    private final WarframeStatsApi api = new WarframeStatsApi(new OkHttpClient.Builder().build());

    private WfsDrops drops = null;
    private Collection<WfsRiven> rivens = null;
    private List<WfsWeapon> weapons = null;
    private WfsWorldState worldState = null;

    public synchronized WfsDrops getDrops() {
        ApiResponse<WfsDrops> response = api.getDrops(WfsDropsRequest.builder().build());
        return drops = response.orElse(ApiResponse.ok(drops)).getResult();
    }

    public synchronized Collection<WfsRiven> getRivens() {
        ApiResponse<Map<String, Map<String, WfsRiven>>> response = api.getRivens(WfsRivensRequest.builder().build());
        return rivens = response.map(this::mapRivens).orElse(ApiResponse.ok(rivens)).getResult();
    }

    private Collection<WfsRiven> mapRivens(Map<String, Map<String, WfsRiven>> map) {
        Collection<WfsRiven> collection = new ArrayList<>();
        for (Map<String, WfsRiven> subMap : map.values()) {
            collection.addAll(subMap.values());
        }
        return collection;
    }

    public synchronized Collection<WfsWeapon> getWeapons() {
        ApiResponse<List<WfsWeapon>> response = api.getWeapons(WfsWeaponsRequest.builder().build());
        return weapons = response.orElse(ApiResponse.ok(weapons)).getResult();
    }

    public synchronized WfsWorldState getWorldState() {
        ApiResponse<WfsWorldState> response = api.getWorldState(WfsWorldStateRequest.builder().build());
        return worldState = response.orElse(ApiResponse.ok(worldState)).getResult();
    }

}
