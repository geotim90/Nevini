package de.nevini.services.warframe;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.WarframeStatsApi;
import de.nevini.api.wfs.model.*;
import de.nevini.api.wfs.requests.WfsDropsRequest;
import de.nevini.api.wfs.requests.WfsInfoRequest;
import de.nevini.api.wfs.requests.WfsRivensRequest;
import de.nevini.api.wfs.requests.WfsWorldStateRequest;
import de.nevini.locators.Locatable;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Service
public class WarframeStatsService implements Locatable {

    private final WarframeStatsApi api = new WarframeStatsApi(new OkHttpClient.Builder().build());

    private WfsDrops drops = null;
    private WfsInfo info = null;
    private Collection<WfsRiven> rivens = null;
    private WfsWorldState worldState = null;

    public synchronized WfsDrops getDrops() {
        ApiResponse<WfsDrops> response = api.getDrops(WfsDropsRequest.builder().build());
        return drops = response.orElse(ApiResponse.ok(drops)).getResult();
    }

    public synchronized WfsInfo getInfo() {
        ApiResponse<WfsInfo> response = api.getInfo(WfsInfoRequest.builder().build());
        return info = response.orElse(ApiResponse.ok(info)).getResult();
    }

    public synchronized Collection<WfsRiven> getRivens() {
        ApiResponse<Map<String, Map<String, WfsRivens>>> response = api.getRivens(WfsRivensRequest.builder().build());
        return rivens = response.map(this::mapRivens).orElse(ApiResponse.ok(rivens)).getResult();
    }

    private Collection<WfsRiven> mapRivens(Map<String, Map<String, WfsRivens>> map) {
        Collection<WfsRiven> collection = new ArrayList<>();
        for (Map<String, WfsRivens> submap : map.values()) {
            for (WfsRivens rivens : submap.values()) {
                if (rivens.getRerolled() != null) {
                    collection.add(rivens.getRerolled());
                }
                collection.add(rivens.getUnrolled());
            }
        }
        return collection;
    }

    public synchronized WfsWorldState getWorldState() {
        ApiResponse<WfsWorldState> response = api.getWorldState(WfsWorldStateRequest.builder().build());
        return worldState = response.orElse(ApiResponse.ok(worldState)).getResult();
    }

}
