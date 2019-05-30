# Module: `osu`

These commands are related to [osu!](https://osu.ppy.sh).

Command | Description
--------|------------
[osu!beatmap](#command-osubeatmap) | displays general information of an osu! beatmap
[osu!best](#command-osubest) | displays the top 100 scores of an osu! user
[osu!events](#command-osuevents) | displays osu! user events
[osu!leaderboard](#command-osuleaderboard) | displays the top 10 osu! users on this server
[osu!recent](#command-osurecent) | displays up to 50 most recent plays over the last 24 hours of an osu! user
[osu!scores](#command-osuscores) | displays the top 100 scores of an osu! beatmap
[osu!stats](#command-osustats) | displays general information of an osu! user

## Command: `osu!beatmap`

Displays general information of an osu! beatmap.

Permission overrides may be applied on node **osu.beatmap**.

Option | Description
-------|------------
--beatmap \<beatmap\> | Refers to an osu! beatmap.

Keyword | Aliases
--------|--------
osu!beatmap | osu!b<br>osu!bm<br>osu!map
--beatmap | //beatmap<br>--bm<br>//bm

## Command: `osu!best`

Displays the top 100 scores of an osu! user.

Permission overrides may be applied on node **osu.best**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user using a mention, id, name, nickname or ign. The `--user` flag is optional if a user mention is used. Refers to the current user if only the `--user` flag is provided.
--mode \<mode\> | Refers to an osu! game mode (osu!, osu!taiko, osu!catch or osu!mania).

Keyword | Aliases
--------|--------
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--mode | //mode

## Command: `osu!events`

Displays osu! user events.

Permission overrides may be applied on node **osu.events**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user using a mention, id, name, nickname or ign. The `--user` flag is optional if a user mention is used. Refers to the current user if only the `--user` flag is provided.
--mode \<mode\> | Refers to an osu! game mode (osu!, osu!taiko, osu!catch or osu!mania).

Keyword | Aliases
--------|--------
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--mode | //mode

Command | Description
--------|------------
[osu!events feed](#command-osuevents-feed) | subscribes to osu! events

### Command: `osu!events feed`

Subscribes to osu! events.

Providing a channel will cause the bot to post osu! events for all guild members in said channel.<br>
If no channel is provided, the bot will stop the feed.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.feed.set**.

Option | Description
-------|------------
\[--channel\] \[\<channel\>\] | Refers to a specific text channel using a channel mention, id or name. The `--channel` flag is optional if a channel mention is used. Refers to the current channel if only the `--channel` flag is provided.

Keyword | Aliases
--------|--------
feed | subscribe
--channel | //channel<br>-c<br>/c

## Command: `osu!leaderboard`

Displays the top 10 osu! users on this server.

Permission overrides may be applied on node **osu.leaderboard**.

Keyword | Aliases
--------|--------
osu!leaderboard | osu!rank<br>osu!ranks

## Command: `osu!recent`

Displays up to 50 most recent plays over the last 24 hours of an osu! user.

Permission overrides may be applied on node **osu.recent**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user using a mention, id, name, nickname or ign. The `--user` flag is optional if a user mention is used. Refers to the current user if only the `--user` flag is provided.
--mode \<mode\> | Refers to an osu! game mode (osu!, osu!taiko, osu!catch or osu!mania).

Keyword | Aliases
--------|--------
osu!recent | osu!recents
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--mode | //mode

Command | Description
--------|------------
[osu!recent feed](#command-osurecent-feed) | subscribes to osu! plays

### Command: `osu!recent feed`

Subscribes to osu! plays.

Providing a channel will cause the bot to post osu! plays for all guild members in said channel.<br>
If no channel is provided, the bot will stop the feed.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.feed.set**.

Option | Description
-------|------------
\[--channel\] \[\<channel\>\] | Refers to a specific text channel using a channel mention, id or name. The `--channel` flag is optional if a channel mention is used. Refers to the current channel if only the `--channel` flag is provided.

Keyword | Aliases
--------|--------
feed | subscribe
--channel | //channel<br>-c<br>/c

## Command: `osu!scores`

Displays the top 100 scores of an osu! beatmap.

Permission overrides may be applied on node **osu.scores**.

Option | Description
-------|------------
--beatmap \<beatmap\> | Refers to an osu! beatmap.
\[--user\] \[\<user\>\] | Refers to a specific user using a mention, id, name, nickname or ign. The `--user` flag is optional if a user mention is used. Refers to the current user if only the `--user` flag is provided.
--mode \<mode\> | Refers to an osu! game mode (osu!, osu!taiko, osu!catch or osu!mania).
--mods \<mods\> | Refers to osu! mods. Examples: `No fail`, `NF`, `HDHR`, `HDHRNCFL`

Keyword | Aliases
--------|--------
--beatmap | //beatmap<br>--bm<br>//bm
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--mode | //mode
--mods | //mods<br>--mod<br>//mod

## Command: `osu!stats`

Displays general information of an osu! user.

Permission overrides may be applied on node **osu.stats**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user using a mention, id, name, nickname or ign. The `--user` flag is optional if a user mention is used. Refers to the current user if only the `--user` flag is provided.
--mode \<mode\> | Refers to an osu! game mode (osu!, osu!taiko, osu!catch or osu!mania).

Keyword | Aliases
--------|--------
osu!stats | osu!user
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--mode | //mode
