package de.nevini.bot.modules.warframe.pricecheck;

import de.nevini.api.wfm.model.items.WfmItemName;
import de.nevini.api.wfm.model.orders.WfmOrder;
import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.warframe.WarframeResolvers;
import de.nevini.bot.scope.Node;
import de.nevini.bot.services.warframe.WarframeMarketService;
import de.nevini.bot.util.Formatter;
import de.nevini.framework.command.CommandOptionDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;

@Component
public class PriceCheckCommand extends Command {

    public PriceCheckCommand() {
        super(CommandDescriptor.builder()
                .keyword("price-check")
                .aliases(new String[]{"pc"})
                .guildOnly(false)
                .node(Node.WARFRAME_PRICE_CHECK)
                .description("performs a price check on a tradable Warframe item using data from warframe.market")
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

        // abort if no data is available
        if (orders == null || orders.isEmpty()) {
            event.reply("No orders found.", event::complete);
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
        String content = "Best offers on warframe.market for **" + item.getItemName()
                + "** as of " + Formatter.formatTimestamp(mostRecent.getLastUpdate())
                + "\n```c"
                + "\n          WTB       WTS"
                + "\nIn game:  " + format6(bestBuyInGame) + "   " + format6(bestSellInGame)
                + "\nOn site:  " + format6(bestBuyOnSite) + "   " + format6(bestSellOnSite)
                + "\nAll:      " + format6(bestBuyAll) + "   " + format6(bestSellAll)
                + "\n```" + (bestBuyInGame == null || bestSellInGame == null
                ? "\n(`-` means that no matching offers were found)" : "");

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

    private String format6(WfmOrder order) {
        if (order == null || order.getPlatinum() == null) {
            return StringUtils.leftPad("-", 6) + " ";
        } else {
            return StringUtils.leftPad(Formatter.formatInteger(order.getPlatinum()), 6) + "p";
        }
    }

}
