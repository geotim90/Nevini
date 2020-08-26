package de.nevini.services.warframe;

import de.nevini.api.wfs.model.weapons.WfsWeapon;
import org.junit.Assert;
import org.junit.Test;

public class WarframeStatsServiceTest {

    @Test
    public void testRepairWeapons() {
        WarframeStatusService service = new WarframeStatusService();
        for (WfsWeapon weapon : service.getWeapons()) {
            Assert.assertNotNull(weapon.getWikiaThumbnail());
            Assert.assertNotNull(weapon.getWikiaUrl());
        }
    }

}
