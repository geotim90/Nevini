# Module: `warframe`

These commands are related to [Warframe](https://www.warframe.com/).

Command | Description
--------|------------
[baro](#command-baro) | displays the current status of the Void Trader using data from warframestat.us
[cycle](#command-cycle) | displays the current World Cycles using data from warframestat.us
[order-book](#command-order-book) | displays the top five rows of the order book for a tradeable Warframe item using data from warframe.market
[price-check](#command-price-check) | performs a price check on a tradeable Warframe item using data from warframe.market
[riven](#command-riven) | displays trading statistics for rivens using data from warframestat.us

## Command: `baro`

Displays the current status of the Void Trader using data from warframestat.us.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **warframe.baro**.

Keyword | Aliases
--------|--------
baro | void-trader

## Command: `cycle`

Displays the current World Cycles using data from warframestat.us.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **warframe.cycle**.

Keyword | Aliases
--------|--------
cycle | cycles<br>world-cycle<br>world-cycles

## Command: `order-book`

Displays the top five rows of the order book for a tradeable Warframe item using data from warframe.market.

Note that only offers of "in game" and "online" users are considered for this command.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **warframe.order-book**.

Option | Description
-------|------------
\[--item\] \<name\> | Refers to an item with a matching name.<br>The `--item` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
order-book | ob
--item | //item

## Command: `price-check`

Performs a price check on a tradeable Warframe item using data from warframe.market.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **warframe.price-check**.

Option | Description
-------|------------
\[--item\] \<name\> | Refers to an item with a matching name.<br>The `--item` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
price-check | pc
--item | //item

## Command: `riven`

Displays trading statistics for rivens using data from warframestat.us.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **warframe.riven**.

Option | Description
-------|------------
\[--riven\] \<type\> | Refers to a riven mod with a matching name.<br>The `--riven` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
riven | rivens
--riven | //riven
