package de.nevini.services.warframe;

import de.nevini.api.wfs.model.weapons.WfsWeapon;
import de.nevini.data.wfs.*;
import org.junit.Assert;
import org.junit.Test;

public class WarframeStatsServiceTest {

    private final WarframeStatusApiProvider apiProvider = new WarframeStatusApiProvider();

    @Test
    public void testRepairWeapons() {
        WarframeStatusService service = new WarframeStatusService(
                new WfsDropDataService(apiProvider),
                new WfsRivenDataService(apiProvider),
                new WfsWeaponDataService(apiProvider),
                new WfsWorldStateDataService(apiProvider)
        );
        for (WfsWeapon weapon : service.getWeapons()) {
            Assert.assertNotNull(weapon.getWikiaThumbnail());
            Assert.assertNotNull(weapon.getWikiaUrl());
        }
    }

}
