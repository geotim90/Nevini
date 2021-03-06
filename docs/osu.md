# Module: `osu`

These commands are related to [osu!](https://osu.ppy.sh).

Command | Description
--------|------------
[osu!beatmap](#command-osubeatmap) | displays general information of osu! beatmaps
[osu!best](#command-osubest) | displays the top 100 scores of an osu! user
[osu!diag](#command-osudiag) | analyses the top 100 scores of an osu! user
[osu!events](#command-osuevents) | displays osu! user events
[osu!leaderboard](#command-osuleaderboard) | displays the top 10 osu! users on this server
[osu!recent](#command-osurecent) | displays up to 50 most recent plays over the last 24 hours of an osu! user
[osu!scores](#command-osuscores) | displays the top 100 scores of an osu! beatmap
[osu!stats](#command-osustats) | displays general information of an osu! user

## Command: `osu!beatmap`

Displays general information of osu! beatmaps.

This command supports search criteria similar to the in-game search function (<https://osu.ppy.sh/help/wiki/Interface#search>):<br>
* `b` - Beatmap id<br>
* `s` - Beatmapset id<br>
* `artist` - Song artist<br>
* `title` - Song title<br>
* `diff` - Beatmap difficulty name / version<br>
* `mode` - Mode - value can be `osu`, `taiko`, `catchthebeat`, or `mania`, or `o/t/c/m` for short<br>
* `creator` - Beatmapset creator<br>
* `status` - Ranked status - value can be `ranked`, `approved`, `pending`, `notsubmitted`, `unknown`, or `loved`, or `r/a/p/n/u/l` for short<br>
* `stars` - Star Difficulty<br>
* `length` - Length in seconds<br>
* `drain` - Drain Time in seconds<br>
* `bpm` - Beats per minute<br>
* `combo` - Max combo<br>
* `pp` - Max performance (without mods)<br>
* `circles` - Number of Circles<br>
* `spinners` - Number of Spinners<br>
* `sliders` - Number of Sliders<br>
* `cs` - Circle Size (CS)<br>
* `hp` - HP Drain Rate (HP)<br>
* `od` - Overall Difficulty (OD)<br>
* `ar` - Approach Rate (AR)<br>
* `aim` - Aim Difficulty<br>
* `speed` - Speed Difficulty<br>
* `rating` - Beatmapset rating<br>
* `favs` - Beatmapset favourite count<br>
* `plays` - Beatmap play count<br>
* `pass` - Beatmap pass count<br>
* `source` - Song source<br>
* `genre` - Song genre<br>
* `language` - Song language<br>
* `tag` - Beatmapset tags<br>
* `hash` - Beatmap hash<br>
* `sort` - The attribute to sort by (one of the above)

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **osu**.

Option | Description
-------|------------
\[--beatmap\] \<beatmap\> | Refers to all osu! beatmaps with a matching id or name.<br>The `--beatmap` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
osu!beatmap | osu!b<br>osu!beatmaps<br>osu!beatmapset<br>osu!bm<br>osu!bms<br>osu!filter<br>osu!find<br>osu!map<br>osu!maps<br>osu!mapset<br>osu!s<br>osu!search<br>osu!set
--beatmap | //beatmap<br>--bm<br>//bm

## Command: `osu!best`

Displays the top 100 scores of an osu! user.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **osu**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific osu! user with a matching mention, id, name, nickname or in-game name.<br>Only supports osu! user ids and names when used via direct message.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--mode \<mode\> | Refers to an osu! game mode with a matching name (`osu!`, `osu!taiko`, `osu!catch` or `osu!mania`).

Keyword | Aliases
--------|--------
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--mode | //mode

## Command: `osu!diag`

Analyses the top 100 scores of an osu! user.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **osu**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific osu! user with a matching mention, id, name, nickname or in-game name.<br>Only supports osu! user ids and names when used via direct message.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--mode \<mode\> | Refers to an osu! game mode with a matching name (`osu!`, `osu!taiko`, `osu!catch` or `osu!mania`).

Keyword | Aliases
--------|--------
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--mode | //mode

## Command: `osu!events`

Displays osu! user events.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **osu**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific osu! user with a matching mention, id, name, nickname or in-game name.<br>Only supports osu! user ids and names when used via direct message.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--mode \<mode\> | Refers to an osu! game mode with a matching name (`osu!`, `osu!taiko`, `osu!catch` or `osu!mania`).

Keyword | Aliases
--------|--------
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--mode | //mode

Command | Description
--------|------------
[osu!events feed](#command-osuevents-feed) | subscribes to osu! events

### Command: `osu!events feed`

Subscribes to osu! events.

This command will behave the same as **feed set osu.events**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.feed.set**.

Option | Description
-------|------------
\[--channel\] \[\<channel\>\] | Refers to a specific text channel with a matching mention, id or name.<br>The `--channel` flag is optional if a channel mention is used.<br>Refers to the current channel if only the `--channel` flag is provided.

Keyword | Aliases
--------|--------
feed | subscribe
--channel | //channel<br>-c<br>/c

## Command: `osu!leaderboard`

Displays the top 10 osu! users on this server.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **osu**.

Keyword | Aliases
--------|--------
osu!leaderboard | osu!rank<br>osu!ranks

## Command: `osu!recent`

Displays up to 50 most recent plays over the last 24 hours of an osu! user.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **osu**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific osu! user with a matching mention, id, name, nickname or in-game name.<br>Only supports osu! user ids and names when used via direct message.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--mode \<mode\> | Refers to an osu! game mode with a matching name (`osu!`, `osu!taiko`, `osu!catch` or `osu!mania`).

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

This command will behave the same as **feed set osu.recent**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.feed.set**.

Option | Description
-------|------------
\[--channel\] \[\<channel\>\] | Refers to a specific text channel with a matching mention, id or name.<br>The `--channel` flag is optional if a channel mention is used.<br>Refers to the current channel if only the `--channel` flag is provided.

Keyword | Aliases
--------|--------
feed | subscribe
--channel | //channel<br>-c<br>/c

## Command: `osu!scores`

Displays the top 100 scores of an osu! beatmap.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **osu**.

Option | Description
-------|------------
\[--beatmap\] \<beatmap\> | Refers to an osu! beatmap with a matching id or name.<br>The `--beatmap` flag is optional if this option is provided first.
\[--user\] \[\<user\>\] | Refers to a specific osu! user with a matching mention, id, name, nickname or in-game name.<br>Only supports osu! user ids and names when used via direct message.<br>The `--user` flag is optional if a user mention is used.<br>Refers to the current user if only the `--user` flag is provided.
--mode \<mode\> | Refers to an osu! game mode with a matching name (`osu!`, `osu!taiko`, `osu!catch` or `osu!mania`).
--mods \<mods\> | Refers to osu! mods with a matching name (e.g. "No Fail") or short code (e.g. `NF`, `HDHR`, `HDHRNCFL`).

Keyword | Aliases
--------|--------
--beatmap | //beatmap<br>--bm<br>//bm
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--mode | //mode
--mods | //mods<br>--mod<br>//mod

## Command: `osu!stats`

Displays general information of an osu! user.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **osu**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific osu! user with a matching mention, id, name, nickname or in-game name.<br>Only supports osu! user ids and names when used via direct message.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--mode \<mode\> | Refers to an osu! game mode with a matching name (`osu!`, `osu!taiko`, `osu!catch` or `osu!mania`).

Keyword | Aliases
--------|--------
osu!stats | osu!user<br>osu!profile
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--mode | //mode
