package de.nevini.services.warframe;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.WarframeStatsApi;
import de.nevini.api.wfs.model.drops.WfsDrop;
import de.nevini.api.wfs.model.rivens.WfsRiven;
import de.nevini.api.wfs.model.weapons.WfsWeapon;
import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.api.wfs.requests.WfsDropsRequest;
import de.nevini.api.wfs.requests.WfsRivensRequest;
import de.nevini.api.wfs.requests.WfsWeaponsRequest;
import de.nevini.api.wfs.requests.WfsWorldStateRequest;
import de.nevini.locators.Locatable;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WarframeStatsService implements Locatable {

    private final WarframeStatsApi api = new WarframeStatsApi(new OkHttpClient.Builder().build());

    private List<WfsDrop> drops = null;
    private Collection<WfsRiven> rivens = null;
    private List<WfsWeapon> weapons = null;
    private WfsWorldState worldState = null;

    public Collection<WfsDrop> getDrops() {
        ApiResponse<List<WfsDrop>> response = api.getDrops(WfsDropsRequest.builder().build());
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
        return weapons = repairWeaponData(response.orElse(ApiResponse.ok(weapons)).getResult());
    }

    private List<WfsWeapon> repairWeaponData(List<WfsWeapon> data) {
        for (WfsWeapon weapon : data) {
            if (StringUtils.isEmpty(weapon.getWikiaThumbnail())) {
                weapon.setWikiaThumbnail(getWeaponWikiaThumbnail(weapon.getName()));
            }
            if (StringUtils.isEmpty(weapon.getWikiaUrl())) {
                weapon.setWikiaUrl(getWeaponWikiaUrl(weapon.getName()));
            }
        }
        return data;
    }

    private String getWeaponWikiaThumbnail(String name) {
        switch (name) {
            case "Ceramic Dagger":
                return "https://vignette.wikia.nocookie.net/warframe/images/6/64/SwordCeramic_d.png";
            case "Dark Split-Sword":
                return "https://vignette.wikia.nocookie.net/warframe/images/d/da/DarkSplitSwordDualIcon.png";
            case "Deth Machine Rifle Prime":
                return "https://vignette.wikia.nocookie.net/warframe/images/4/4e/DMR_Prime.png";
            case "Dual Ether":
                return "https://vignette.wikia.nocookie.net/warframe/images/e/ee/Dual_Ether.png";
            case "Galatine":
                return "https://vignette.wikia.nocookie.net/warframe/images/d/df/Galatine_o.png";
            case "Kuva Bramma":
                return "https://vignette.wikia.nocookie.net/warframe/images/a/a7/Kuva_Bramma.png";
            case "Miter":
                return "https://vignette.wikia.nocookie.net/warframe/images/e/e4/Miter.png";
            case "Sybaris Prime":
                return "https://vignette.wikia.nocookie.net/warframe/images/8/8b/Sybaris_Prime.png";
            case "Wolf Sledge":
                return "https://vignette.wikia.nocookie.net/warframe/images/4/46/Wolf_Sledge_Image.png";
            default:
                log.info("Weapon missing wikia thumbnail: {}", name);
                return null;
        }
    }

    private String getWeaponWikiaUrl(String name) {
        switch (name) {
            case "Dark Split-Sword":
                return "https://warframe.fandom.com/wiki/Dark_Split-Sword";
            default:
                log.info("Weapon missing wikia url: {}", name);
                return null;
        }
    }

    public synchronized WfsWorldState getWorldState() {
        ApiResponse<WfsWorldState> response = api.getWorldState(WfsWorldStateRequest.builder().build());
        return worldState = response.orElse(ApiResponse.ok(worldState)).getResult();
    }

}
