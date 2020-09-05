package de.nevini.modules.warframe.services;

import de.nevini.core.locators.Locatable;
import de.nevini.modules.warframe.api.wfm.model.items.WfmItemName;
import de.nevini.modules.warframe.api.wfm.model.orders.WfmOrder;
import de.nevini.modules.warframe.api.wfm.model.statistics.WfmStatisticsPayload;
import de.nevini.modules.warframe.cache.wfm.WfmItemNameDataService;
import de.nevini.modules.warframe.cache.wfm.WfmOrderDataService;
import de.nevini.modules.warframe.cache.wfm.WfmStatisticsDataService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class WarframeMarketService implements Locatable {

    private final WfmItemNameDataService itemNameDataService;
    private final WfmOrderDataService orderDataService;
    private final WfmStatisticsDataService statisticsDataService;

    public WarframeMarketService(
            @Autowired WfmItemNameDataService itemNameDataService,
            @Autowired WfmOrderDataService orderDataService,
            @Autowired WfmStatisticsDataService statisticsDataService
    ) {
        this.itemNameDataService = itemNameDataService;
        this.orderDataService = orderDataService;
        this.statisticsDataService = statisticsDataService;
    }

    public Collection<WfmItemName> getItemNames() {
        return itemNameDataService.get();
    }

    public Collection<WfmOrder> getOrders(@NonNull WfmItemName item) {
        return orderDataService.get(item.getUrlName());
    }

    public WfmStatisticsPayload getItemStatistics(@NonNull WfmItemName item) {
        return statisticsDataService.get(item.getUrlName());
    }

}
