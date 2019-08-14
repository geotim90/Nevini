package de.nevini.services.warframe;

import de.nevini.api.wfm.model.items.WfmItemName;
import de.nevini.api.wfm.model.orders.WfmOrder;
import de.nevini.api.wfm.model.statistics.WfmStatisticsPayload;
import de.nevini.data.wfm.WfmItemNameDataService;
import de.nevini.data.wfm.WfmOrdersDataService;
import de.nevini.data.wfm.WfmStatisticsDataService;
import de.nevini.locators.Locatable;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class WarframeMarketService implements Locatable {

    private final WfmItemNameDataService itemNameDataService;
    private final WfmOrdersDataService orderDataService;
    private final WfmStatisticsDataService statisticsDataService;

    public WarframeMarketService(
            @Autowired WfmItemNameDataService itemNameDataService,
            @Autowired WfmOrdersDataService orderDataService,
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
