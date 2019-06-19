package de.nevini.bot.modules.warframe.orderbook;

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

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderBookCommand extends Command {

    public OrderBookCommand() {
        super(CommandDescriptor.builder()
                .keyword("order-book")
                .aliases(new String[]{"ob"})
                .guildOnly(false)
                .node(Node.WARFRAME_ORDER_BOOK)
                .description("displays the top five rows of the order book for a tradable Warframe item "
                        + "using data from warframe.market")
                .options(new CommandOptionDescriptor[]{
                        WarframeResolvers.ITEM.describe(false, true)
                })
                .details("Note that only offers of \"in game\" and \"online\" users are considered for this command.")
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
            event.reply("No price information found.", event::complete);
            return;
        }

        // find most recent offer
        WfmOrder mostRecent = orders.stream().filter(e -> !Boolean.FALSE.equals(e.getVisible()))
                .max(Comparator.comparing(WfmOrder::getLastUpdate)).orElseThrow(IllegalStateException::new);

        // maps containing details per price point
        Map<Integer, Integer> buyCount = new HashMap<>();
        Map<Integer, Integer> buyVolume = new HashMap<>();
        Map<Integer, Integer> sellVolume = new HashMap<>();
        Map<Integer, Integer> sellCount = new HashMap<>();

        // populate maps
        populateMaps("buy", buyCount, buyVolume, orders);
        populateMaps("sell", sellCount, sellVolume, orders);

        // get top five prices
        final int rows = 5;
        List<Integer> bestBuyPrices = buyCount.keySet().stream()
                .sorted(Comparator.reverseOrder()).limit(rows).collect(Collectors.toList());
        List<Integer> bestSellPrices = sellCount.keySet().stream()
                .sorted(Comparator.naturalOrder()).limit(rows).collect(Collectors.toList());

        // generate output
        StringBuilder builder = new StringBuilder(
                "Best offers on warframe.market for **" + item.getItemName() + "** as of "
                        + Formatter.formatTimestamp(mostRecent.getLastUpdate())
                        + "\n<https://warframe.market/items/" + item.getUrlName() + ">"
                        + "\n```c"
                        + "\n--------- WTB ---------   --------- WTS ---------"
                        + "\nOffers  Volume  Price     Price    Volume  Offers"
                        + "\n-----------------------   -----------------------");
        for (int i = 0; i < rows; ++i) {
            String buySide = "                       ";
            String sellSide = "                       ";
            if (bestBuyPrices.size() > i) {
                Integer price = bestBuyPrices.get(i);
                buySide = formatVolume(buyCount.get(price)) + "  "
                        + formatVolume(buyVolume.get(price)) + "  "
                        + formatPrice(price);
            }
            if (bestSellPrices.size() > i) {
                Integer price = bestSellPrices.get(i);
                sellSide = formatPrice(price) + "  "
                        + formatVolume(sellVolume.get(price)) + "  "
                        + formatVolume(sellCount.get(price));
            }
            builder.append('\n').append(buySide).append("   ").append(sellSide);
        }
        builder.append("\n```");

        // send message
        event.reply(builder.toString(), event::complete);
    }

    private void populateMaps(
            String orderType, Map<Integer, Integer> count, Map<Integer, Integer> volume, Collection<WfmOrder> orders
    ) {
        orders.stream().filter(e -> !Boolean.FALSE.equals(e.getVisible())
                && orderType.equals(e.getOrderType())
                && !"offline".equals(e.getUser().getStatus())
        ).forEach(e -> {
            count.merge(e.getPlatinum(), 1, (k, v) -> v + 1);
            volume.merge(e.getPlatinum(), e.getQuantity(), (k, v) -> v + e.getQuantity());
        });
    }

    private String formatVolume(Integer value) {
        if (value == null || value == 0) {
            return "     -";
        } else {
            return StringUtils.leftPad(Formatter.formatInteger(value), 6);
        }
    }

    private String formatPrice(Integer value) {
        if (value == null || value == 0) {
            return "     - ";
        } else {
            return StringUtils.leftPad(Formatter.formatInteger(value), 6) + 'p';
        }
    }

}
