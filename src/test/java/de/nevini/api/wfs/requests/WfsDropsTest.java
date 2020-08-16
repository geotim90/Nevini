package de.nevini.api.wfs.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.model.drops.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class WfsDropsTest extends WfsApiProvider {

    @Test
    public void testParser() throws IOException {
        WfsDropsRequest request = WfsDropsRequest.builder().build();
        WfsDrops result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("all.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all data is present
        Assert.assertEquals(20, result.getMissionRewards().size());
        Assert.assertEquals(1132, result.getRelics().size());
        Assert.assertEquals(10, result.getTransientRewards().size());
        Assert.assertEquals(545, result.getModLocations().size());
        Assert.assertEquals(559, result.getEnemyModTables().size());
        Assert.assertEquals(108, result.getBlueprintLocations().size());
        Assert.assertEquals(57, result.getEnemyBlueprintTables().size());
        Assert.assertEquals(17, result.getSortieRewards().size());
        Assert.assertEquals(17, result.getKeyRewards().size());
        Assert.assertEquals(7, result.getCetusBountyRewards().size());
        Assert.assertEquals(14, result.getSolarisBountyRewards().size());

        // make sure all data was parsed correctly - rotational mission
        WfsMission mission = result.getMissionRewards().get("Mercury").get("Apollodorus");
        Assert.assertEquals("Survival", mission.getGameMode());
        Assert.assertEquals(Boolean.FALSE, mission.getIsEvent());
        Assert.assertEquals(3, mission.getRewards().getRotationRewards().size());
        Assert.assertEquals(2, mission.getRewards().getRotationRewards().get("A").size());
        WfsReward reward = mission.getRewards().getRotationRewards().get("A").get(0);
        Assert.assertEquals("83b8ed8608ef404c5ceeef9ef2906af1", reward.getId());
        Assert.assertEquals("2,000 Credits Cache", reward.getItemName());
        Assert.assertEquals("Common", reward.getRarity());
        Assert.assertEquals(Float.valueOf(50), reward.getChance());

        // make sure all data was parsed correctly - non-rotational mission
        mission = result.getMissionRewards().get("Mercury").get("Elion");
        Assert.assertEquals("Capture", mission.getGameMode());
        Assert.assertEquals(Boolean.FALSE, mission.getIsEvent());
        Assert.assertEquals(15, mission.getRewards().getRewards().size());
        reward = mission.getRewards().getRewards().get(0);
        Assert.assertEquals("c0400ac7082c2f3d811e47ca9b7a8ae8", reward.getId());
        Assert.assertEquals("Vitality", reward.getItemName());
        Assert.assertEquals("Uncommon", reward.getRarity());
        Assert.assertEquals(Float.valueOf(10.84f), reward.getChance());

        // make sure all data was parsed correctly - relic
        WfsRelic relic = result.getRelics().get(0);
        Assert.assertEquals("4e4d817f8c2d887316c6d8add253403e", relic.getId());
        Assert.assertEquals("Axi", relic.getTier());
        Assert.assertEquals("Intact", relic.getState());
        Assert.assertEquals(6, relic.getRewards().size());
        reward = relic.getRewards().get(0);
        Assert.assertEquals("6733cc5298452209aa29dd72027c7df1", reward.getId());
        Assert.assertEquals("Akstiletto Prime Barrel", reward.getItemName());
        Assert.assertEquals("Uncommon", reward.getRarity());
        Assert.assertEquals(Float.valueOf(11), reward.getChance());

        // make sure all data was parsed correctly - objective
        WfsObjective objective = result.getTransientRewards().get(0);
        Assert.assertEquals("a2aacef993a37aaa3d7ce4fc22b823f8", objective.getId());
        Assert.assertEquals("Arbitrations", objective.getObjectiveName());
        Assert.assertEquals(35, objective.getRewards().size());
        WfsRotationReward rotationReward = objective.getRewards().get(0);
        Assert.assertEquals("05ca9bc1521d5a11e5467136478121f2", rotationReward.getId());
        Assert.assertEquals("A", rotationReward.getRotation());
        Assert.assertEquals("Ayatan Valana Sculpture", rotationReward.getItemName());
        Assert.assertEquals("Rare", rotationReward.getRarity());
        Assert.assertEquals(Float.valueOf(9), rotationReward.getChance());

        // make sure all data was parsed correctly - mod location
        WfsModLocation modLocation = result.getModLocations().get(0);
        Assert.assertEquals("26e902a203e561023110f39382a29b4f", modLocation.getId());
        Assert.assertEquals("True Steel", modLocation.getModName());
        Assert.assertEquals(25, modLocation.getEnemies().size());
        WfsEnemy enemy = modLocation.getEnemies().get(0);
        Assert.assertEquals("d1a700b3bd61387bc63568e10e2f65ad", enemy.getId());
        Assert.assertEquals("Kuva Heavy Gunner", enemy.getEnemyName());
        Assert.assertEquals(Float.valueOf(10), enemy.getEnemyModDropChance());
        Assert.assertEquals("Uncommon", enemy.getRarity());
        Assert.assertEquals(Float.valueOf(25.29f), enemy.getChance());

        // make sure all data was parsed correctly - enemy mod table
        WfsEnemyModTable enemyModTable = result.getEnemyModTables().get(0);
        Assert.assertEquals("fbb9f5f0c4b0a80c96bc30765b11a583", enemyModTable.getId());
        Assert.assertEquals("Terra Oxium Osprey", enemyModTable.getEnemyName());
        Assert.assertEquals(Float.valueOf(5f), enemyModTable.getEnemyModDropChance());
        Assert.assertEquals(12, enemyModTable.getMods().size());
        WfsMod mod = enemyModTable.getMods().get(0);
        Assert.assertEquals("c0400ac7082c2f3d811e47ca9b7a8ae8", mod.getId());
        Assert.assertEquals("Vitality", mod.getModName());
        Assert.assertEquals("Uncommon", mod.getRarity());
        Assert.assertEquals(Float.valueOf(25.29f), mod.getChance());

        // make sure all data was parsed correctly - blueprint location
        WfsBlueprintLocation blueprintLocation = result.getBlueprintLocations().get(0);
        Assert.assertEquals("eb83b4c0c00d87c70fc14eb4004f7e80", blueprintLocation.getId());
        Assert.assertEquals("Hildryn Neuroptics Blueprint", blueprintLocation.getItemName());
        Assert.assertEquals("Hildryn Neuroptics Blueprint", blueprintLocation.getBlueprintName());
        Assert.assertEquals(1, blueprintLocation.getEnemies().size());
        enemy = blueprintLocation.getEnemies().get(0);
        Assert.assertEquals("bac19a4a88ccdfd9e1ce0f525b45fdcd", enemy.getId());
        Assert.assertEquals("Exploiter Orb", enemy.getEnemyName());
        Assert.assertEquals(Float.valueOf(100), enemy.getEnemyItemDropChance());
        Assert.assertEquals(Float.valueOf(100), enemy.getEnemyBlueprintDropChance());
        Assert.assertEquals("Common", enemy.getRarity());
        Assert.assertEquals(Float.valueOf(38.72f), enemy.getChance());

        // make sure all data was parsed correctly - enemy blueprint table
        WfsEnemyBlueprintTable enemyBlueprintTable = result.getEnemyBlueprintTables().get(0);
        Assert.assertEquals("a09beb3e2a5d3ab9a8314998981633ab", enemyBlueprintTable.getId());
        Assert.assertEquals("Shik Tal", enemyBlueprintTable.getEnemyName());
        Assert.assertEquals(Float.valueOf(25f), enemyBlueprintTable.getEnemyItemDropChance());
        Assert.assertEquals(Float.valueOf(25f), enemyBlueprintTable.getBlueprintDropChance());
        Assert.assertEquals(1, enemyBlueprintTable.getItems().size());
        WfsItem item = enemyBlueprintTable.getItems().get(0);
        Assert.assertEquals("866ac7784d30cdcf236f0bf3839710a0", item.getId());
        Assert.assertEquals("Brakk Barrel", item.getItemName());
        Assert.assertEquals("Common", item.getRarity());
        Assert.assertEquals(Float.valueOf(100), item.getChance());
        Assert.assertEquals(1, enemyBlueprintTable.getMods().size());
        mod = enemyBlueprintTable.getMods().get(0);
        Assert.assertEquals("866ac7784d30cdcf236f0bf3839710a0", mod.getId());
        Assert.assertEquals("Brakk Barrel", mod.getModName());
        Assert.assertEquals("Common", mod.getRarity());
        Assert.assertEquals(Float.valueOf(100), mod.getChance());

        // make sure all data was parsed correctly - sortie
        item = result.getSortieRewards().get(0);
        Assert.assertEquals("e16beeb5ce4fc97a4b02ad245fa6d58d", item.getId());
        Assert.assertEquals("Rifle Riven Mod", item.getItemName());
        Assert.assertEquals("Rare", item.getRarity());
        Assert.assertEquals(Float.valueOf(6.79f), item.getChance());

        // make sure all data was parsed correctly - key
        WfsKey key = result.getKeyRewards().get(0);
        Assert.assertEquals("f8a5482637296393dfbff7e75d3a1ff7", key.getId());
        Assert.assertEquals("Recover The Orokin Archive", key.getKeyName());
        Assert.assertEquals(3, key.getRewards().size());
        Assert.assertEquals(16, key.getRewards().get("A").size());
        item = key.getRewards().get("A").get(0);
        Assert.assertEquals("c0400ac7082c2f3d811e47ca9b7a8ae8", item.getId());
        Assert.assertEquals("Vitality", item.getItemName());
        Assert.assertEquals("Uncommon", item.getRarity());
        Assert.assertEquals(Float.valueOf(15.18f), item.getChance());

        // make sure all data was parsed correctly - cetus bounty
        WfsBounty bounty = result.getCetusBountyRewards().get(0);
        Assert.assertEquals("6f9f3333023cf3b4929e3e5604705ee8", bounty.getId());
        Assert.assertEquals("Level 5 - 15 Cetus Bounty", bounty.getBountyLevel());
        Assert.assertEquals(3, bounty.getRewards().size());
        Assert.assertEquals(26, bounty.getRewards().get("A").size());
        WfsStageReward stageReward = bounty.getRewards().get("A").get(0);
        Assert.assertEquals("1b6498e23a3c43775699129e7ebfcfeb", stageReward.getId());
        Assert.assertEquals("Redirection", stageReward.getItemName());
        Assert.assertEquals("Uncommon", stageReward.getRarity());
        Assert.assertEquals(Float.valueOf(20), stageReward.getChance());
        Assert.assertEquals("Stage 1", stageReward.getStage());

        // make sure all data was parsed correctly - solaris bounty
        bounty = result.getSolarisBountyRewards().get(0);
        Assert.assertEquals("97f1a5efc46b180fcc3b65af6fe0d9bf", bounty.getId());
        Assert.assertEquals("Level 5 - 15 Orb Vallis Bounty", bounty.getBountyLevel());
        Assert.assertEquals(3, bounty.getRewards().size());
        Assert.assertEquals(26, bounty.getRewards().get("A").size());
        stageReward = bounty.getRewards().get("A").get(0);
        Assert.assertEquals("3ee9736f9f90fd21b467ad879b162af0", stageReward.getId());
        Assert.assertEquals("100X Oxium", stageReward.getItemName());
        Assert.assertEquals("Uncommon", stageReward.getRarity());
        Assert.assertEquals(Float.valueOf(20), stageReward.getChance());
        Assert.assertEquals("Stage 1", stageReward.getStage());
    }

    @Test
    public void testBlankRequest() {
        ApiResponse<WfsDrops> response = getWfsApi().getDrops(WfsDropsRequest.builder().build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
