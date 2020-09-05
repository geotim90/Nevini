package de.nevini.modules.warframe.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
@Value
public class WfsWorldState {

    @SerializedName("timestamp")
    OffsetDateTime timestamp;

    @SerializedName("news")
    List<WfsNews> news;

    @SerializedName("events")
    List<WfsEvent> events;

    @SerializedName("alerts")
    List<WfsAlert> alerts;

    @SerializedName("sortie")
    WfsSortie sortie;

    @SerializedName("syndicateMissions")
    List<WfsSyndicateMissions> syndicateMissions;

    @SerializedName("fissures")
    List<WfsFissure> fissures;

    @SerializedName("globalUpgrades")
    List<WfsGlobalUpgrade> globalUpgrades;

    @SerializedName("invasions")
    List<WfsInvasion> invasions;

    @SerializedName("voidTrader")
    WfsVoidTrader voidTrader;

    @SerializedName("dailyDeals")
    List<WfsDailyDeal> dailyDeals;

    @SerializedName("simaris")
    WfsSimaris simaris;

    @SerializedName("conclaveChallenges")
    List<WfsConclaveChallenge> conclaveChallenges;

    @SerializedName("cetusCycle")
    WfsCetusCycle cetusCycle;

    @SerializedName("constructionProgress")
    WfsConstructionProgress constructionProgress;

    @SerializedName("vallisCycle")
    WfsVallisCycle vallisCycle;

    @SerializedName("nightwave")
    WfsNightwave nightwave;

    @SerializedName("arbitration")
    WfsArbitration arbitration;

    @SerializedName("sentientOutposts")
    WfsSentientOutposts sentientOutposts;

}
