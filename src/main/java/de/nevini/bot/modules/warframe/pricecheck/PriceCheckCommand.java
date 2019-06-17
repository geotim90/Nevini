package de.nevini.bot.modules.warframe.pricecheck;

import de.nevini.api.wfm.model.items.WfmItemName;
import de.nevini.api.wfm.model.orders.WfmOrder;
import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.warframe.WarframeResolvers;
import de.nevini.bot.scope.Node;
import de.nevini.bot.scope.Permissions;
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
                .aliases(new String[]{"pricecheck", "pc"})
                .guildOnly(false)
                .node(Node.WARFRAME_PRICE_CHECK)
                .minimumBotPermissions(Permissions.TALK)
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
        WfmOrder bestBuyOffline = getBestBuy(orders, "offline");
        WfmOrder bestBuyOnline = getBestBuy(orders, "online");
        WfmOrder bestBuyInGame = getBestBuy(orders, "ingame");
        WfmOrder bestSellOffline = getBestSell(orders, "offline");
        WfmOrder bestSellOnline = getBestSell(orders, "online");
        WfmOrder bestSellInGame = getBestSell(orders, "ingame");

        // find most recent offer
        WfmOrder mostRecent = orders.stream().filter(e -> !Boolean.FALSE.equals(e.getVisible()))
                .max(Comparator.comparing(WfmOrder::getLastUpdate)).orElseThrow(IllegalStateException::new);

        // format results
        String content = "Best offers on warframe.market for " + item.getItemName()
                + " as of " + Formatter.formatTimestamp(mostRecent.getLastUpdate())
                + "\n```c"
                + "\n           WTB       WTS"
                + "\ningame:    " + format6(bestBuyInGame) + "p   " + format6(bestSellInGame) + "p"
                + "\nonline:    " + format6(bestBuyOnline) + "p   " + format6(bestSellOnline) + "p"
                + "\noffline:   " + format6(bestBuyOffline) + "p   " + format6(bestSellOffline) + "p"
                + "\n```";

        // send message
        event.reply(content, event::complete);
    }

    private WfmOrder getBestBuy(Collection<WfmOrder> orders, String status) {
        return orders.stream()
                .filter(e -> !Boolean.FALSE.equals(e.getVisible())
                        && "buy".equals(e.getOrderType())
                        && status.equals(e.getUser().getStatus()))
                .max(Comparator.comparing(WfmOrder::getPlatinum))
                .orElse(null);
    }

    private WfmOrder getBestSell(Collection<WfmOrder> orders, String status) {
        return orders.stream()
                .filter(e -> !Boolean.FALSE.equals(e.getVisible())
                        && "sell".equals(e.getOrderType())
                        && status.equals(e.getUser().getStatus()))
                .min(Comparator.comparing(WfmOrder::getPlatinum))
                .orElse(null);
    }

    private String format6(WfmOrder order) {
        if (order == null || order.getPlatinum() == null) {
            return StringUtils.leftPad("N/A", 6);
        } else {
            return StringUtils.leftPad(Formatter.formatInteger(order.getPlatinum()), 6);
        }
    }

}
