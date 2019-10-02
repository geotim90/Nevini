# Module: `guild`

These commands help manage a server (aka. guild) and monitor user (aka. member) activity. This modules is active by default but can be deactivated.

Command | Description
--------|------------
[activity](#command-activity) | displays and configures user activity information
[auto-role](#command-auto-role) | displays and configures auto-roles
[feed](#command-feed) | displays and configures feeds
[ign](#command-ign) | displays and configures in-game names
[inactivity](#command-inactivity) | displays and configures user inactivity information
[tribute](#command-tribute) | displays and configures user contributions

## Command: `activity`

Displays and configures user activity information.

By default, this command will behave the same as **activity get**.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[activity get](#command-activity-get) | displays user, role and/or game activity information
[activity set](#command-activity-set) | configures user activity information
[activity unset](#command-activity-unset) | configures user activity information
[activity report](#command-activity-report) | displays an activity report for the entire server or just a single user

### Command: `activity get`

Displays user, role and/or game activity information.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **guild.activity.get**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a role mention is used.
--game \<game\> | Refers to a specific game with a matching id or name.

Keyword | Aliases
--------|--------
get | display<br>echo<br>list<br>print<br>show
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--role | //role<br>-r<br>/r
--game | //game

Command | Description
--------|------------
[activity get message](#command-activity-get-message) | displays user activity information for when they last posted a message in this Discord server
[activity get online](#command-activity-get-online) | displays user activity information for when they were last online on Discord
[activity get playing](#command-activity-get-playing) | displays user game activity information

#### Command: `activity get message`

Displays user activity information for when they last posted a message in this Discord server.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **guild.activity.get**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
message | last-message<br>lastmessage
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

#### Command: `activity get online`

Displays user activity information for when they were last online on Discord.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **guild.activity.get**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
online | last-online<br>lastonline
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

#### Command: `activity get playing`

Displays user game activity information.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **guild.activity.get**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--game \<game\> | Refers to a specific game with a matching id or name.

Keyword | Aliases
--------|--------
playing | played<br>last-played<br>lastplayed
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--game | //game

### Command: `activity set`

Configures user activity information.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[activity set online](#command-activity-set-online) | configures user activity information for when they were last online on Discord
[activity set message](#command-activity-set-message) | configures user activity information for when they last posted a message in this Discord server
[activity set playing](#command-activity-set-playing) | configures user game activity information

#### Command: `activity set online`

Configures user activity information for when they were last online on Discord.

Note that timestamps provided via this command do not override *real* activity information. Instead, they just provided a manual minimum value for activity reports on this server.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.activity.set**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--time \<timestamp\> | A valid ISO 8601 UTC timestamp (e.g. `1999-12-31T23:59:59.999`).<br>`now` can be used as a shortcut for the current date and time.<br>Relative input such as `5 days ago` or `-24h` can also be used.

Keyword | Aliases
--------|--------
online | last-online<br>lastonline
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--time | //time<br>--timestamp<br>//timestamp<br>-t<br>/t

#### Command: `activity set message`

Configures user activity information for when they last posted a message in this Discord server.

Note that timestamps provided via this command do not override *real* activity information. Instead, they just provided a manual minimum value for activity reports on this server.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.activity.set**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--time \<timestamp\> | A valid ISO 8601 UTC timestamp (e.g. `1999-12-31T23:59:59.999`).<br>`now` can be used as a shortcut for the current date and time.<br>Relative input such as `5 days ago` or `-24h` can also be used.

Keyword | Aliases
--------|--------
message | last-message<br>lastmessage
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--time | //time<br>--timestamp<br>//timestamp<br>-t<br>/t

#### Command: `activity set playing`

Configures user game activity information.

Note that timestamps provided via this command do not override *real* activity information. Instead, they just provided a manual minimum value for activity reports on this server.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.activity.set**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--game \<game\> | Refers to a specific game with a matching id or name.
--time \<timestamp\> | A valid ISO 8601 UTC timestamp (e.g. `1999-12-31T23:59:59.999`).<br>`now` can be used as a shortcut for the current date and time.<br>Relative input such as `5 days ago` or `-24h` can also be used.

Keyword | Aliases
--------|--------
playing | played<br>last-played<br>lastplayed
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--game | //game
--time | //time<br>--timestamp<br>//timestamp<br>-t<br>/t

### Command: `activity unset`

Configures user activity information.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[activity unset online](#command-activity-unset-online) | configures user activity information for when they were last online on Discord
[activity unset message](#command-activity-unset-message) | configures user activity information for when they last posted a message in this Discord server
[activity unset playing](#command-activity-unset-playing) | configures user game activity information

#### Command: `activity unset online`

Configures user activity information for when they were last online on Discord.

Note that this command only removes timestamps provided via the **set** command and does not override *real* activity information.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.activity.set**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
online | last-online<br>lastonline
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

#### Command: `activity unset message`

Configures user activity information for when they last posted a message in this Discord server.

Note that this command only removes timestamps provided via the **set** command and does not override *real* activity information.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.activity.set**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--time \<timestamp\> | A valid ISO 8601 UTC timestamp (e.g. `1999-12-31T23:59:59.999`).<br>`now` can be used as a shortcut for the current date and time.<br>Relative input such as `5 days ago` or `-24h` can also be used.

Keyword | Aliases
--------|--------
message | last-message<br>lastmessage
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--time | //time<br>--timestamp<br>//timestamp<br>-t<br>/t

#### Command: `activity unset playing`

Configures user game activity information.

Note that this command only removes timestamps provided via the **set** command and does not override *real* activity information.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.activity.set**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--game \<game\> | Refers to a specific game with a matching id or name.

Keyword | Aliases
--------|--------
playing | played<br>last-played<br>lastplayed
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--game | //game

### Command: `activity report`

Displays an activity report for the entire server or just a single user.

By default, you need the **Manage Server** permission to execute this command with `--user`.<br>
Permission overrides for `--user` may be applied on node **guild.report.user**.

By default, you need the **Manage Server** permission to execute this command with `--role`.<br>
Permission overrides for `--role` may be applied on node **guild.report.role**.

By default, you need the **Manage Server** permission to execute this command with `--server`.<br>
Permission overrides for `--server` may be applied on node **guild.report.server**.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **guild.report.self**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a role mention is used.
--server | Refers to the current server.

Keyword | Aliases
--------|--------
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--role | //role<br>-r<br>/r
--server | //server<br>--guild<br>//guild<br>-s<br>/s<br>-g<br>/g

## Command: `auto-role`

Displays and configures auto-roles.

By default, this command will behave the same as **auto-role get**.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
auto-role | auto-roles<br>autorole<br>autoroles<br>ar

Command | Description
--------|------------
[auto-role get](#command-auto-role-get) | displays the currently configured auto-roles
[auto-role set](#command-auto-role-set) | configures auto-roles
[auto-role unset](#command-auto-role-unset) | removes auto-roles

### Command: `auto-role get`

Displays the currently configured auto-roles.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.get**.

Keyword | Aliases
--------|--------
get | display<br>echo<br>list<br>print<br>show

### Command: `auto-role set`

Configures auto-roles.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
set | add<br>start<br>+

Command | Description
--------|------------
[auto-role set join](#command-auto-role-set-join) | configures auto-roles for users that join the server
[auto-role set playing](#command-auto-role-set-playing) | configures auto-roles for users that are currently playing a certain game
[auto-role set plays](#command-auto-role-set-plays) | configures auto-roles for users that ever play a certain game
[auto-role set veteran](#command-auto-role-set-veteran) | configures auto-roles for users that joined the server some time ago
[auto-role set voice](#command-auto-role-set-voice) | configures auto-roles for users that are currently in a specific voice channel

#### Command: `auto-role set join`

Configures auto-roles for users that join the server.

Restrictions on who can be assigned join roles may be applied on node **guild.auto-role.join** (only server level overrides are applicable).

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.set**.

Option | Description
-------|------------
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a role mention is used or this option is provided first.

Keyword | Aliases
--------|--------
--role | //role<br>-r<br>/r

#### Command: `auto-role set playing`

Configures auto-roles for users that are currently playing a certain game.

Restrictions on who can be assigned playing roles may be applied on node **guild.auto-role.playing** (only server level overrides are applicable).

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.set**.

Option | Description
-------|------------
--game \<game\> | Refers to a specific game with a matching id or name.
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a role mention is used or this option is provided first.

Keyword | Aliases
--------|--------
--game | //game
--role | //role<br>-r<br>/r

#### Command: `auto-role set plays`

Configures auto-roles for users that ever play a certain game.

Restrictions on who can be assigned plays roles may be applied on node **guild.auto-role.playing** (only server level overrides are applicable).

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.set**.

Option | Description
-------|------------
--game \<game\> | Refers to a specific game with a matching id or name.
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a role mention is used or this option is provided first.

Keyword | Aliases
--------|--------
--game | //game
--role | //role<br>-r<br>/r

#### Command: `auto-role set veteran`

Configures auto-roles for users that joined the server some time ago.

Restrictions on who can be assigned veteran roles may be applied on node **guild.auto-role.veteran** (only server level overrides are applicable).

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.set**.

Option | Description
-------|------------
\[--duration\] \<days\> | A number of days.<br>Input such as `5 days` or `2w` can also be used.<br>The `--duration` flag is optional if this option is provided first.
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a role mention is used.

Keyword | Aliases
--------|--------
--duration | //duration<br>--days<br>//days<br>-d<br>/d
--role | //role<br>-r<br>/r

#### Command: `auto-role set voice`

Configures auto-roles for users that are currently in a specific voice channel.

Restrictions on who can be assigned voice roles may be applied on node **guild.auto-role.voice** (only server level overrides are applicable).

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.set**.

Option | Description
-------|------------
\[--channel\] \[\<channel\>\] | Refers to a specific voice channel with a matching mention, id or name.<br>The `--channel` flag is optional if a channel mention is used.<br>Refers to the current channel if only the `--channel` flag is provided.
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a role mention is used or this option is provided first.

Keyword | Aliases
--------|--------
--channel | //channel<br>-c<br>/c
--role | //role<br>-r<br>/r

### Command: `auto-role unset`

Removes auto-roles.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
unset | remove<br>stop<br>-

Command | Description
--------|------------
[auto-role unset join](#command-auto-role-unset-join) | stops auto-roles for users that join the server
[auto-role unset playing](#command-auto-role-unset-playing) | stops auto-roles for users that are currently playing a certain game
[auto-role unset plays](#command-auto-role-unset-plays) | stops auto-roles for users that ever play a certain game
[auto-role unset veteran](#command-auto-role-unset-veteran) | stops auto-roles for users that joined the server some time ago
[auto-role unset voice](#command-auto-role-unset-voice) | stops auto-roles for users that are currently in a specific voice channel

#### Command: `auto-role unset join`

Stops auto-roles for users that join the server.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.set**.

#### Command: `auto-role unset playing`

Stops auto-roles for users that are currently playing a certain game.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.set**.

Option | Description
-------|------------
\[--game\] \<game\> | Refers to a specific game with a matching id or name.<br>The `--game` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
--game | //game

#### Command: `auto-role unset plays`

Stops auto-roles for users that ever play a certain game.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.set**.

Option | Description
-------|------------
\[--game\] \<game\> | Refers to a specific game with a matching id or name.<br>The `--game` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
--game | //game

#### Command: `auto-role unset veteran`

Stops auto-roles for users that joined the server some time ago.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.set**.

Option | Description
-------|------------
\[--duration\] \<days\> | A number of days.<br>Input such as `5 days` or `2w` can also be used.<br>The `--duration` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
--duration | //duration<br>--days<br>//days<br>-d<br>/d

#### Command: `auto-role unset voice`

Stops auto-roles for users that are currently in a specific voice channel.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.set**.

Option | Description
-------|------------
\[--game\] \<game\> | Refers to a specific game with a matching id or name.<br>The `--game` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
--game | //game

## Command: `feed`

Displays and configures feeds.

By default, this command will behave the same as **feed get**.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
feed | feeds<br>subscription<br>subscriptions

Command | Description
--------|------------
[feed get](#command-feed-get) | displays a list of feeds
[feed set](#command-feed-set) | starts automatic feeds
[feed unset](#command-feed-unset) | stops automatic feeds

### Command: `feed get`

Displays a list of feeds.

Providing the `--all` flag will display a list of all possible feeds.<br>
Providing a feed will display whether a subscription is active and in which channel.<br>
Providing a channel will display active subscriptions in that channel.<br>
Providing no options will display a list of active subscriptions on this server.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.feed.get**.

Option | Description
-------|------------
\[--feed\] \<type\> | Refers to a specific feed type with a matching name.<br>The `--feed` flag is optional if this option is provided first.
\[--channel\] \[\<channel\>\] | Refers to a specific text channel with a matching mention, id or name.<br>The `--channel` flag is optional if a channel mention is used.<br>Refers to the current channel if only the `--channel` flag is provided.
--all | Explicitly refers to all possible feeds.

Keyword | Aliases
--------|--------
get | display<br>echo<br>list<br>print<br>show
--feed | //feed<br>-f<br>/f
--channel | //channel<br>-c<br>/c
--all | //all<br>-a<br>/a

### Command: `feed set`

Starts automatic feeds.

This command will cause the bot to post automatic feed updates in the provided channel.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.feed.set**.

Option | Description
-------|------------
\[--feed\] \<type\> | Refers to a specific feed type with a matching name.<br>The `--feed` flag is optional if this option is provided first.
\[--channel\] \[\<channel\>\] | Refers to a specific text channel with a matching mention, id or name.<br>The `--channel` flag is optional if a channel mention is used.<br>Refers to the current channel if only the `--channel` flag is provided.

Keyword | Aliases
--------|--------
set | add<br>start<br>subscribe<br>sub<br>+
--feed | //feed<br>-f<br>/f
--channel | //channel<br>-c<br>/c

### Command: `feed unset`

Stops automatic feeds.

This command will stop automatic feed updates for a specific feed.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.feed.set**.

Option | Description
-------|------------
\[--feed\] \<type\> | Refers to a specific feed type with a matching name.<br>The `--feed` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
unset | remove<br>stop<br>unsubscribe<br>unsub<br>-
--feed | //feed<br>-f<br>/f

## Command: `ign`

Displays and configures in-game names.

By default, this command will behave the same as **ign get**.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[ign get](#command-ign-get) | displays in-game names for a user and/or game
[ign set](#command-ign-set) | configures the in-game name for a specific user in a specific game
[ign unset](#command-ign-unset) | removes the in-game name for a specific user in a specific game
[ign missing](#command-ign-missing) | finds users missing in-game names for a game

### Command: `ign get`

Displays in-game names for a user and/or game.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **guild.ign.get**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--game \<game\> | Refers to a specific game with a matching id or name.

Keyword | Aliases
--------|--------
get | display<br>echo<br>list<br>print<br>show
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--game | //game

### Command: `ign set`

Configures the in-game name for a specific user in a specific game.

Users can only configure in-game names for users whose highest role is lower than their highest role.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Nicknames** permission to execute this command.<br>
Permission overrides may be applied on node **guild.ign.set**.

Option | Description
-------|------------
\[--name\] \<name\> | The in-game name to use. The flag is optional if this option is provided first.
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used.<br>Refers to the current user if only the `--user` flag is provided.
--game \<game\> | Refers to a specific game with a matching id or name.

Keyword | Aliases
--------|--------
--name | //name
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--game | //game

### Command: `ign unset`

Removes the in-game name for a specific user in a specific game.

Users can only configure in-game names for users whose highest role is lower than their highest role.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Nicknames** permission to execute this command.<br>
Permission overrides may be applied on node **guild.ign.set**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used.<br>Refers to the current user if only the `--user` flag is provided.
--game \<game\> | Refers to a specific game with a matching id or name.

Keyword | Aliases
--------|--------
unset | remove<br>clear<br>reset
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--game | //game

### Command: `ign missing`

Finds users missing in-game names for a game.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **guild.ign.get**.

Option | Description
-------|------------
\[--game\] \<game\> | Refers to a specific game with a matching id or name.<br>The `--game` flag is optional if this option is provided first.
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a role mention is used.

Keyword | Aliases
--------|--------
--game | //game
--role | //role<br>-r<br>/r

## Command: `inactivity`

Displays and configures user inactivity information.

By default, this command will behave the same as **inactivity get**.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[inactivity get](#command-inactivity-get) | displays user inactivity thresholds
[inactivity set](#command-inactivity-set) | configures user inactivity thresholds
[inactivity unset](#command-inactivity-unset) | removes user inactivity thresholds

### Command: `inactivity get`

Displays user inactivity thresholds.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.inactivity.get**.

Keyword | Aliases
--------|--------
get | display<br>echo<br>list<br>print<br>show

Command | Description
--------|------------
[inactivity get message](#command-inactivity-get-message) | displays the user inactivity threshold for when they last posted a message in this Discord server
[inactivity get online](#command-inactivity-get-online) | displays the user inactivity threshold for when they were last online on Discord
[inactivity get playing](#command-inactivity-get-playing) | displays user game inactivity thresholds

#### Command: `inactivity get message`

Displays the user inactivity threshold for when they last posted a message in this Discord server.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.inactivity.get**.

Keyword | Aliases
--------|--------
message | last-message<br>lastmessage

#### Command: `inactivity get online`

Displays the user inactivity threshold for when they were last online on Discord.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.inactivity.get**.

Keyword | Aliases
--------|--------
online | last-online<br>lastonline

#### Command: `inactivity get playing`

Displays user game inactivity thresholds.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.inactivity.get**.

Option | Description
-------|------------
--game \<game\> | Refers to a specific game with a matching id or name.

Keyword | Aliases
--------|--------
playing | played<br>last-played<br>lastplayed
--game | //game

### Command: `inactivity set`

Configures user inactivity thresholds.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[inactivity set online](#command-inactivity-set-online) | configures the user inactivity threshold for when they were last online on Discord
[inactivity set message](#command-inactivity-set-message) | configures the user inactivity threshold for when they last posted a message in this Discord server
[inactivity set playing](#command-inactivity-set-playing) | configures a user game inactivity threshold

#### Command: `inactivity set online`

Configures the user inactivity threshold for when they were last online on Discord.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.inactivity.set**.

Option | Description
-------|------------
\[--duration\] \<days\> | A number of days.<br>Input such as `5 days` or `2w` can also be used.<br>The `--duration` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
online | last-online<br>lastonline
--duration | //duration<br>--days<br>//days<br>-d<br>/d

#### Command: `inactivity set message`

Configures the user inactivity threshold for when they last posted a message in this Discord server.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.inactivity.set**.

Option | Description
-------|------------
\[--duration\] \<days\> | A number of days.<br>Input such as `5 days` or `2w` can also be used.<br>The `--duration` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
message | last-message<br>lastmessage
--duration | //duration<br>--days<br>//days<br>-d<br>/d

#### Command: `inactivity set playing`

Configures a user game inactivity threshold.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.inactivity.set**.

Option | Description
-------|------------
\[--duration\] \<days\> | A number of days.<br>Input such as `5 days` or `2w` can also be used.<br>The `--duration` flag is optional if this option is provided first.
--game \<game\> | Refers to a specific game with a matching id or name.

Keyword | Aliases
--------|--------
playing | played<br>last-played<br>lastplayed
--duration | //duration<br>--days<br>//days<br>-d<br>/d
--game | //game

### Command: `inactivity unset`

Removes user inactivity thresholds.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
unset | remove

Command | Description
--------|------------
[inactivity unset online](#command-inactivity-unset-online) | removes the user inactivity threshold for when they were last online on Discord
[inactivity unset message](#command-inactivity-unset-message) | removes the user inactivity threshold for when they last posted a message in this Discord server
[inactivity unset playing](#command-inactivity-unset-playing) | removes a user game inactivity threshold

#### Command: `inactivity unset online`

Removes the user inactivity threshold for when they were last online on Discord.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.inactivity.set**.

Keyword | Aliases
--------|--------
online | last-online<br>lastonline

#### Command: `inactivity unset message`

Removes the user inactivity threshold for when they last posted a message in this Discord server.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.inactivity.set**.

Keyword | Aliases
--------|--------
message | last-message<br>lastmessage

#### Command: `inactivity unset playing`

Removes a user game inactivity threshold.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.inactivity.set**.

Option | Description
-------|------------
\[--game\] \<game\> | Refers to a specific game with a matching id or name.<br>The `--game` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
playing | played<br>last-played<br>lastplayed
--game | //game

## Command: `tribute`

Displays and configures user contributions.

By default, this command will behave the same as **tribute get**.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
tribute | contribution<br>donation

Command | Description
--------|------------
[tribute get](#command-tribute-get) | displays user contributions
[tribute set](#command-tribute-set) | confirms user contributions
[tribute unset](#command-tribute-unset) | revokes confirmation of user contributions
[tribute role](#command-tribute-role) | displays and configures the role for users that need to contribute
[tribute timeout](#command-tribute-timeout) | displays and configures the timeout for users that need to contribute
[tribute start](#command-tribute-start) | displays and configures the timestamp from which the tribute timeout is checked
[tribute delay](#command-tribute-delay) | displays and configures delayed tribute timeouts for individual users
[tribute report](#command-tribute-report) | displays a tribute report for the entire server or just a single user

### Command: `tribute get`

Displays user contributions.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.get**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
get | display<br>echo<br>list<br>print<br>show
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

### Command: `tribute set`

Confirms user contributions.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.set**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
set | confirm
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

### Command: `tribute unset`

Revokes confirmation of user contributions.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.set**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
unset | revoke<br>reset<br>clear<br>reset<br>undo
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

### Command: `tribute role`

Displays and configures the role for users that need to contribute.

By default, this command will behave the same as **tribute role get**.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[tribute role get](#command-tribute-role-get) | displays the role for users that need to contribute
[tribute role set](#command-tribute-role-set) | configures the role for users that need to contribute
[tribute role unset](#command-tribute-role-unset) | removes the configured role for users that need to contribute

#### Command: `tribute role get`

Displays the role for users that need to contribute.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.role.get**.

Keyword | Aliases
--------|--------
get | display<br>echo<br>list<br>print<br>show

#### Command: `tribute role set`

Configures the role for users that need to contribute.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.role.set**.

Option | Description
-------|------------
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a role mention is used or this option is provided first.

Keyword | Aliases
--------|--------
--role | //role<br>-r<br>/r

#### Command: `tribute role unset`

Removes the configured role for users that need to contribute.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.role.set**.

Keyword | Aliases
--------|--------
unset | remove

### Command: `tribute timeout`

Displays and configures the timeout for users that need to contribute.

By default, this command will behave the same as **tribute timeout get**.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
timeout | deadline

Command | Description
--------|------------
[tribute timeout get](#command-tribute-timeout-get) | displays the timeout for users that need to contribute
[tribute timeout set](#command-tribute-timeout-set) | configures the timeout for users that need to contribute
[tribute timeout unset](#command-tribute-timeout-unset) | removes the configured timeout for users that need to contribute

#### Command: `tribute timeout get`

Displays the timeout for users that need to contribute.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.timeout.get**.

Keyword | Aliases
--------|--------
get | display<br>echo<br>list<br>print<br>show

#### Command: `tribute timeout set`

Configures the timeout for users that need to contribute.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.timeout.set**.

Option | Description
-------|------------
\[--duration\] \<days\> | A number of days.<br>Input such as `5 days` or `2w` can also be used.<br>The `--duration` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
--duration | //duration<br>--days<br>//days<br>-d<br>/d

#### Command: `tribute timeout unset`

Removes the configured timeout for users that need to contribute.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.timeout.set**.

Keyword | Aliases
--------|--------
unset | remove<br>clear<br>reset

### Command: `tribute start`

Displays and configures the timestamp from which the tribute timeout is checked.

By default, this command will behave the same as **tribute start get**.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
start | joined<br>tracked

Command | Description
--------|------------
[tribute start get](#command-tribute-start-get) | displays the timestamp from which the tribute timeout is checked
[tribute start set](#command-tribute-start-set) | configures the timestamp from which the tribute timeout is checked
[tribute start unset](#command-tribute-start-unset) | clears the timestamp from which the tribute timeout is checked

#### Command: `tribute start get`

Displays the timestamp from which the tribute timeout is checked.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.start.get**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
get | display<br>echo<br>list<br>print<br>show
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

#### Command: `tribute start set`

Configures the timestamp from which the tribute timeout is checked.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.start.set**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--time \<timestamp\> | A valid ISO 8601 UTC timestamp (e.g. `1999-12-31T23:59:59.999`).<br>`now` can be used as a shortcut for the current date and time.<br>Relative input such as `5 days ago` or `-24h` can also be used.

Keyword | Aliases
--------|--------
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--time | //time<br>--timestamp<br>//timestamp<br>-t<br>/t

#### Command: `tribute start unset`

Clears the timestamp from which the tribute timeout is checked.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.start.set**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
unset | remove<br>clear<br>reset
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

### Command: `tribute delay`

Displays and configures delayed tribute timeouts for individual users.

By default, this command will behave the same as **tribute delay get**.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
delay | extend<br>postpone

Command | Description
--------|------------
[tribute delay get](#command-tribute-delay-get) | displays delayed tribute timeouts for individual users
[tribute delay set](#command-tribute-delay-set) | configures delayed tribute timeouts for individual users
[tribute delay unset](#command-tribute-delay-unset) | clears delayed tribute timeouts for individual users

#### Command: `tribute delay get`

Displays delayed tribute timeouts for individual users.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.delay.get**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
get | display<br>echo<br>list<br>print<br>show
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

#### Command: `tribute delay set`

Configures delayed tribute timeouts for individual users.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.delay.set**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--duration \<days\> | A number of days.<br>Input such as `5 days` or `2w` can also be used.

Keyword | Aliases
--------|--------
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--duration | //duration<br>--days<br>//days<br>-d<br>/d

#### Command: `tribute delay unset`

Clears delayed tribute timeouts for individual users.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.tribute.start.set**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
unset | remove<br>clear<br>reset
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

### Command: `tribute report`

Displays a tribute report for the entire server or just a single user.

By default, you need the **Manage Server** permission to execute this command with `--user`.<br>
Permission overrides for `--user` may be applied on node **guild.report.user**.

By default, you need the **Manage Server** permission to execute this command with `--role`.<br>
Permission overrides for `--role` may be applied on node **guild.report.role**.

By default, you need the **Manage Server** permission to execute this command with `--server`.<br>
Permission overrides for `--server` may be applied on node **guild.report.server**.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **guild.report.self**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a role mention is used.
--server | Refers to the current server.

Keyword | Aliases
--------|--------
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--role | //role<br>-r<br>/r
--server | //server<br>--guild<br>//guild<br>-s<br>/s<br>-g<br>/g
