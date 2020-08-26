package de.nevini.data.wfs;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.WarframeStatusApi;
import de.nevini.api.wfs.model.weapons.WfsWeapon;
import de.nevini.api.wfs.requests.WfsWeaponsRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WfsWeaponDataService {

    private static final String KEY = "pc|en";

    private final Cache<String, List<WfsWeapon>> readCache;
    private final WarframeStatusApi api;
    private final Map<String, List<WfsWeapon>> backup;

    public WfsWeaponDataService(@Autowired WarframeStatusApiProvider apiProvider) {
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(15))
                .build();
        this.api = apiProvider.getApi();
        this.backup = new ConcurrentHashMap<>();
    }

    public List<WfsWeapon> get() {
        log.trace("get()");
        return getFromReadCache().orElseGet(() ->
                getFromApi().orElse(getFromBackup())
        );
    }

    private @NonNull Optional<List<WfsWeapon>> getFromReadCache() {
        log.trace("getFromReadCache()");
        return Optional.ofNullable(readCache.getIfPresent(KEY));
    }

    private @NonNull Optional<List<WfsWeapon>> getFromApi() {
        log.trace("getFromApi()");
        ApiResponse<List<WfsWeapon>> response = api.getWeapons(WfsWeaponsRequest.builder().build());
        List<WfsWeapon> result = response.getResult();
        if (result != null && result.size() > 0) {
            return Optional.of(cache(repairWeaponData(result)));
        }
        return Optional.empty();
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

    private @NonNull List<WfsWeapon> cache(@NonNull List<WfsWeapon> names) {
        log.debug("Cache data: {}", names);
        readCache.put(KEY, names);
        backup.put(KEY, names);
        return names;
    }

    private List<WfsWeapon> getFromBackup() {
        log.trace("getFromBackup()");
        return backup.get(KEY);
    }

}
