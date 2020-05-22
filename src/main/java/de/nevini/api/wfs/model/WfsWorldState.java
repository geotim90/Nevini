package de.nevini.api.wfs.model;

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

    @SerializedName("cetusCycle")
    WfsCetusCycle cetusCycle;

    @SerializedName("vallisCycle")
    WfsVallisCycle vallisCycle;

    @SerializedName("arbitration")
    WfsArbitration arbitration;

    @SerializedName("sentientOutposts")
    WfsSentientOutposts sentientOutposts;

}
