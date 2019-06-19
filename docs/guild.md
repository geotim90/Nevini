# Module: `guild`

These commands help manage a server (aka. guild) and monitor user (aka. member) activity. This modules is active by default but can be deactivated.

Command | Description
--------|------------
[activity](#command-activity) | displays user and/or game activity information
[auto-role](#command-auto-role) | displays and configures auto-roles
[feed](#command-feed) | displays and configures feeds
[find](#command-find) | finds users, roles etc. by any of their identifiers
[ign](#command-ign) | displays and configures in-game names

## Command: `activity`

Displays user and/or game activity information.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **guild.activity**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--game \<game\> | Refers to a specific game with a matching id or name.

Keyword | Aliases
--------|--------
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--game | //game

## Command: `auto-role`

Displays and configures auto-roles.

By default, this command will behave the same as **auto-role get**.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
auto-role | auto-roles<br>ar

Command | Description
--------|------------
[auto-role get](#command-auto-role-get) | displays the currently configured auto-roles
[auto-role set](#command-auto-role-set) | configures auto-roles

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

#### Command: `auto-role set join`

Configures auto-roles for users that join the server.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.set**.

Option | Description
-------|------------
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used or this option is provided first.

Keyword | Aliases
--------|--------
--role | //role<br>-r<br>/r

#### Command: `auto-role set playing`

Configures auto-roles for users that are currently playing a certain game.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.set**.

Option | Description
-------|------------
--game \<game\> | Refers to a specific game with a matching id or name.
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used or this option is provided first.

Keyword | Aliases
--------|--------
--game | //game
--role | //role<br>-r<br>/r

#### Command: `auto-role set plays`

Configures auto-roles for users that ever play a certain game.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **guild.auto-role.set**.

Option | Description
-------|------------
--game \<game\> | Refers to a specific game with a matching id or name.
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used or this option is provided first.

Keyword | Aliases
--------|--------
--game | //game
--role | //role<br>-r<br>/r

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
Permission overrides may be applied on node **guild.feed.unset**.

Option | Description
-------|------------
\[--feed\] \<type\> | Refers to a specific feed type with a matching name.<br>The `--feed` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
unset | remove<br>stop<br>unsubscribe<br>unsub<br>-
--feed | //feed<br>-f<br>/f

## Command: `find`

Finds users, roles etc. by any of their identifiers.

By default, this command will behave the same as **find user**.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
find | search

Command | Description
--------|------------
[find user](#command-find-user) | finds users by any of their identifiers
[find role](#command-find-role) | finds roles by any of their identifiers
[find permission](#command-find-permission) | finds permission by any of their identifiers
[find node](#command-find-node) | finds nodes by any of their identifiers
[find module](#command-find-module) | finds modules by any of their identifiers
[find game](#command-find-game) | finds games by any of their identifiers
[find channel](#command-find-channel) | finds text channels by any of their identifiers

### Command: `find user`

Finds users by any of their identifiers.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **guild.find.user**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to all users with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
user | member<br>u<br>m
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

### Command: `find role`

Finds roles by any of their identifiers.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **guild.find.role**.

Option | Description
-------|------------
\[--role\] \<role\> | Refers to all roles with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used or this option is provided first.

Keyword | Aliases
--------|--------
role | r
--role | //role<br>-r<br>/r

### Command: `find permission`

Finds permission by any of their identifiers.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.find.module**.

Option | Description
-------|------------
\[--permission\] \<permission\> | Refers to all permissions with a matching name (e.g. "Manage Server").<br>The `--permission` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
--permission | //permission<br>--perm<br>//perm<br>-p<br>/p

### Command: `find node`

Finds nodes by any of their identifiers.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.find.module**.

Option | Description
-------|------------
\[--node\] \<node\> | Refers to all permission nodes for bot commands with a matching name.<br>The `--node` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
--node | //node<br>-n<br>/n

### Command: `find module`

Finds modules by any of their identifiers.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **guild.find.module**.

Option | Description
-------|------------
\[--module\] \<module\> | Refers to all bot modules with a matching name.<br>The `--module` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
--module | //module

### Command: `find game`

Finds games by any of their identifiers.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **guild.find.game**.

Option | Description
-------|------------
\[--game\] \<game\> | Refers to all games with a matching id or name.<br>The `--game` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
--game | //game

### Command: `find channel`

Finds text channels by any of their identifiers.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **guild.find.channel**.

Option | Description
-------|------------
\[--channel\] \[\<channel\>\] | Refers to all text channels with a matching mention, id or name.<br>The `--channel` flag is optional if a channel mention is used or this option is provided first.<br>Refers to the current channel if only the `--channel` flag is provided.

Keyword | Aliases
--------|--------
channel | c
--channel | //channel<br>-c<br>/c

## Command: `ign`

Displays and configures in-game names.

By default, this command will behave the same as **ign get**.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[ign get](#command-ign-get) | displays in-game names for a user and/or game
[ign set](#command-ign-set) | configures the in-game name for a specific user in a specific game

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
