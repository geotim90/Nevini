package de.nevini.modules.warframe.commands.pricecheck;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfm.model.items.WfmItemName;
import de.nevini.modules.warframe.api.wfm.model.orders.WfmOrder;
import de.nevini.modules.warframe.api.wfm.model.statistics.WfmStatisticsLiveEntry;
import de.nevini.modules.warframe.api.wfm.model.statistics.WfmStatisticsPayload;
import de.nevini.modules.warframe.resolvers.WarframeResolvers;
import de.nevini.modules.warframe.services.WarframeMarketService;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Component
public class PriceCheckCommand extends Command {

    public PriceCheckCommand() {
        super(CommandDescriptor.builder()
                .keyword("price-check")
                .aliases(new String[]{"pc", "wtb", "wts"})
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("performs a price check on a tradeable Warframe item using data from warframe.market")
                .options(new CommandOptionDescriptor[]{
                        WarframeResolvers.ITEM.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        WarframeResolvers.ITEM.resolveArgumentOrOptionOrInput(event, item -> acceptItem(event, item));
    }

    private void acceptItem(CommandEvent event, WfmItemName item) {
        // retrieve item orders from service
        WarframeMarketService service = event.locate(WarframeMarketService.class);
        Collection<WfmOrder> orders = service.getOrders(item);
        WfmStatisticsPayload statistics = service.getItemStatistics(item);

        // abort if no data is available
        if (orders == null || orders.isEmpty()) {
            event.reply("No price information found.", event::complete);
            return;
        }

        // find best offers
        WfmOrder bestBuyInGame = getBestBuy(orders, "ingame", null);
        WfmOrder bestBuyOnSite = getBestBuy(orders, "online", bestBuyInGame);
        WfmOrder bestBuyAll = getBestBuy(orders, "offline", bestBuyOnSite);
        WfmOrder bestSellInGame = getBestSell(orders, "ingame", null);
        WfmOrder bestSellOnSite = getBestSell(orders, "online", bestSellInGame);
        WfmOrder bestSellAll = getBestSell(orders, "offline", bestSellOnSite);

        // find most recent offer
        WfmOrder mostRecent = orders.stream().filter(e -> !Boolean.FALSE.equals(e.getVisible()))
                .max(Comparator.comparing(WfmOrder::getLastUpdate)).orElseThrow(IllegalStateException::new);

        // format results
        String content = "Best offers on warframe.market for **" + item.getItemName() + "** as of "
                + Formatter.formatTimestamp(mostRecent.getLastUpdate())
                + "\n<https://warframe.market/items/" + item.getUrlName() + ">"
                + "\n```c"
                + "\n          WTB       WTS"
                + "\nIn game:  " + formatPrice(bestBuyInGame) + "   " + formatPrice(bestSellInGame)
                + "\nOn site:  " + formatPrice(bestBuyOnSite) + "   " + formatPrice(bestSellOnSite)
                + "\nAll:      " + formatPrice(bestBuyAll) + "   " + formatPrice(bestSellAll)
                + "\n```" + (bestBuyInGame == null || bestSellInGame == null
                ? "\n(`-` means that no matching offers were found)" : "");

        // append statistics if available
        if (statistics != null && statistics.getLive() != null && statistics.getLive().getLast48hours() != null) {
            List<WfmStatisticsLiveEntry> entries = statistics.getLive().getLast48hours();
            WfmStatisticsLiveEntry lastBuyStats = entries.stream().filter(e -> "buy".equals(e.getOrderType()))
                    .max(Comparator.comparing(WfmStatisticsLiveEntry::getDateTime)).orElse(null);
            WfmStatisticsLiveEntry lastSellStats = entries.stream().filter(e -> "sell".equals(e.getOrderType()))
                    .max(Comparator.comparing(WfmStatisticsLiveEntry::getDateTime)).orElse(null);
            if (lastBuyStats != null && lastSellStats != null) {
                content += "\n\nStatistics on warframe.market for **" + item.getItemName() + "** as of "
                        + Formatter.formatTimestamp(lastSellStats.getDateTime())
                        + "\n<https://warframe.market/items/" + item.getUrlName() + "/statistics>"
                        + "\n```c"
                        + "\n                   WTB          WTS"
                        + "\nAverage:           " + formatPrice(lastBuyStats.getAvgPrice())
                        + "   " + formatPrice(lastSellStats.getAvgPrice())
                        + "\nMoving average:    " + formatPrice(lastBuyStats.getMovingAvg())
                        + "   " + formatPrice(lastSellStats.getMovingAvg())
                        + "\nWeighted average:  " + formatPrice(lastBuyStats.getWaPrice())
                        + "   " + formatPrice(lastSellStats.getWaPrice())
                        + "\nMedian:            " + formatPrice(lastBuyStats.getMedian())
                        + "   " + formatPrice(lastSellStats.getMedian())
                        + "\nBest:              " + formatPrice(lastBuyStats.getMaxPrice())
                        + "   " + formatPrice(lastSellStats.getMinPrice())
                        + "\nWorst:             " + formatPrice(lastBuyStats.getMinPrice())
                        + "   " + formatPrice(lastSellStats.getMaxPrice())
                        + "\nVolume:            " + formatVolume(lastBuyStats.getVolume())
                        + "   " + formatVolume(lastSellStats.getVolume())
                        + "\n                   " + formatRatio(lastBuyStats.getVolume(), lastSellStats.getVolume())
                        + "   " + formatRatio(lastSellStats.getVolume(), lastBuyStats.getVolume())
                        + "\n```";
            }
        }

        // send message
        event.reply(content, event::complete);
    }

    private WfmOrder getBestBuy(Collection<WfmOrder> orders, String status, WfmOrder floor) {
        WfmOrder best = orders.stream()
                .filter(e -> !Boolean.FALSE.equals(e.getVisible())
                        && "buy".equals(e.getOrderType())
                        && status.equals(e.getUser().getStatus()))
                .max(Comparator.comparing(WfmOrder::getPlatinum))
                .orElse(null);
        if (floor != null && (best == null || floor.getPlatinum() > best.getPlatinum())) {
            return floor;
        } else {
            return best;
        }
    }

    private WfmOrder getBestSell(Collection<WfmOrder> orders, String status, WfmOrder ceil) {
        WfmOrder best = orders.stream()
                .filter(e -> !Boolean.FALSE.equals(e.getVisible())
                        && "sell".equals(e.getOrderType())
                        && status.equals(e.getUser().getStatus()))
                .min(Comparator.comparing(WfmOrder::getPlatinum))
                .orElse(null);
        if (ceil != null && (best == null || ceil.getPlatinum() < best.getPlatinum())) {
            return ceil;
        } else {
            return best;
        }
    }

    private String formatPrice(WfmOrder order) {
        if (order == null || order.getPlatinum() == null) {
            return "     - ";
        } else {
            return StringUtils.leftPad(Formatter.formatInteger(order.getPlatinum()), 6) + "p";
        }
    }

    private String formatPrice(Integer value) {
        if (value == null || value == 0) {
            return "     -    ";
        } else {
            return StringUtils.leftPad(Formatter.formatInteger(value), 6) + "   p";
        }
    }

    private String formatPrice(Float value) {
        if (value == null || value == 0) {
            return "     -    ";
        } else {
            String text = Formatter.formatDecimal(value);
            // right pad
            if (!text.contains(".")) {
                text += "   ";
            } else if (text.lastIndexOf('.') == text.length() - 2) {
                text += " ";
            }
            // left pad
            return StringUtils.leftPad(text, 9) + "p";
        }
    }

    private String formatVolume(Integer value) {
        if (value == null || value == 0) {
            return "     -    ";
        } else {
            return StringUtils.leftPad(Formatter.formatInteger(value), 6) + "    ";
        }
    }

    private String formatRatio(Integer a, Integer b) {
        if (a == null && b == null) {
            return "     -    ";
        } else if (a == null) {
            return "     0   %";
        } else if (b == null) {
            return "   100   %";
        } else {
            String text = Formatter.formatPercent((double) a / (a + b));
            // trim % off the end
            text = text.substring(0, text.length() - 1);
            // right pad
            if (!text.contains(".")) {
                text += "   ";
            } else if (text.lastIndexOf('.') == text.length() - 2) {
                text += " ";
            }
            // left pad
            return StringUtils.leftPad(text, 9) + "%";
        }
    }

}
