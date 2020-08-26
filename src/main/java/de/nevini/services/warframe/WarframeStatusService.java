package de.nevini.services.warframe;

import de.nevini.api.wfs.model.drops.WfsDrop;
import de.nevini.api.wfs.model.rivens.WfsRiven;
import de.nevini.api.wfs.model.weapons.WfsWeapon;
import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.data.wfs.WfsDropDataService;
import de.nevini.data.wfs.WfsRivenDataService;
import de.nevini.data.wfs.WfsWeaponDataService;
import de.nevini.data.wfs.WfsWorldStateDataService;
import de.nevini.locators.Locatable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class WarframeStatusService implements Locatable {

    private final WfsDropDataService dropDataService;
    private final WfsRivenDataService rivenDataService;
    private final WfsWeaponDataService weaponDataService;
    private final WfsWorldStateDataService worldStateDataService;

    public WarframeStatusService(
            @Autowired WfsDropDataService dropDataService,
            @Autowired WfsRivenDataService rivenDataService,
            @Autowired WfsWeaponDataService weaponDataService,
            @Autowired WfsWorldStateDataService worldStateDataService
    ) {
        this.dropDataService = dropDataService;
        this.rivenDataService = rivenDataService;
        this.weaponDataService = weaponDataService;
        this.worldStateDataService = worldStateDataService;
    }

    public Collection<WfsDrop> getDrops() {
        return dropDataService.get();
    }

    public Collection<WfsRiven> getRivens() {
        return rivenDataService.get();
    }

    public synchronized Collection<WfsWeapon> getWeapons() {
        return weaponDataService.get();
    }

    public synchronized WfsWorldState getWorldState() {
        return worldStateDataService.get();
    }

}
