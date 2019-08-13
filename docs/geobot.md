# Module: `geobot`

Geobot style command emulation.

Command | Description
--------|------------
[geobot](#command-geobot) | Geobot style command emulation

## Command: `geobot`

Geobot style command emulation.

By default, this command will behave the same as **geobot report** for the current user.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
geobot | geo

Command | Description
--------|------------
[geobot report](#command-geobot-report) | displays an activity report for the entire server or just a single user
[geobot ping](#command-geobot-ping) | measures the bot's communication latency
[geobot help](#command-geobot-help) | provides a list of commands or details on a specific command
[geobot get](#command-geobot-get) | Geobot style `get` command emulation
[geobot set](#command-geobot-set) | Geobot style `set` command emulation
[geobot unset](#command-geobot-unset) | Geobot style `unset` command emulation
[geobot add](#command-geobot-add) | Geobot style `add` command emulation
[geobot remove](#command-geobot-remove) | Geobot style `remove` command emulation

### Command: `geobot report`

Displays an activity report for the entire server or just a single user.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.mod**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

### Command: `geobot ping`

Measures the bot's communication latency.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **core.ping**.

Keyword | Aliases
--------|--------
ping | pong<br>latency

### Command: `geobot help`

Provides a list of commands or details on a specific command.

If a valid command is provided, this will display details on that specific command.<br>
If no valid command is provided, this will display a list of commands.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **core.help**.

Option | Description
-------|------------
\[\<command\>\] | the specific command to look up

Keyword | Aliases
--------|--------
help | about<br>info

### Command: `geobot get`

Geobot style `get` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot get contribution](#command-geobot-get-contribution) | displays user contributions
[geobot get game](#command-geobot-get-game) | Geobot style `get game` command emulation
[geobot get member](#command-geobot-get-member) | Geobot style `get member` command emulation
[geobot get role](#command-geobot-get-role) | Geobot style `get role` command emulation
[geobot get timeout](#command-geobot-get-timeout) | Geobot style `get timeout` command emulation

#### Command: `geobot get contribution`

Displays user contributions.

This command will behave the same as **tribute get**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.mod**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
contribution | tribute<br>donation
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

#### Command: `geobot get game`

Geobot style `get game` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot get game timeout](#command-geobot-get-game-timeout) | displays user game inactivity thresholds

##### Command: `geobot get game timeout`

Displays user game inactivity thresholds.

This command will behave the same as **inactivity get playing**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.mod**.

Option | Description
-------|------------
--game \<game\> | Refers to a specific game with a matching id or name.

Keyword | Aliases
--------|--------
--game | //game

#### Command: `geobot get member`

Geobot style `get member` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot get member joined](#command-geobot-get-member-joined) | displays the timestamp from which the tribute timeout is checked
[geobot get member lastonline](#command-geobot-get-member-lastonline) | displays user activity information for when they were last online on Discord
[geobot get member lastmessage](#command-geobot-get-member-lastmessage) | displays user activity information for when they last posted a message in this Discord server
[geobot get member lastplayed](#command-geobot-get-member-lastplayed) | displays user game activity information

##### Command: `geobot get member joined`

Displays the timestamp from which the tribute timeout is checked.

This command will behave the same as **tribute start get**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.mod**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
joined | start<br>tracked
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

##### Command: `geobot get member lastonline`

Displays user activity information for when they were last online on Discord.

This command will behave the same as **activity get online**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.mod**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
lastonline | last-online<br>online
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

##### Command: `geobot get member lastmessage`

Displays user activity information for when they last posted a message in this Discord server.

This command will behave the same as **activity get message**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.mod**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
lastmessage | last-message<br>message
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

##### Command: `geobot get member lastplayed`

Displays user game activity information.

This command will behave the same as **activity get playing**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.mod**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--game \<game\> | Refers to a specific game with a matching id or name.

Keyword | Aliases
--------|--------
lastplayed | last-played<br>playing<br>played
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--game | //game

#### Command: `geobot get role`

Geobot style `get role` command emulation.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
role | roles

Command | Description
--------|------------
[geobot get role initiate](#command-geobot-get-role-initiate) | displays the role for users that need to contribute
[geobot get role member](#command-geobot-get-role-member) | displays the roles for users to include in inactivity reports
[geobot get role mod](#command-geobot-get-role-mod) | displays server level role permission overrides for node **geobot.mod**
[geobot get role admin](#command-geobot-get-role-admin) | displays server level role permission overrides for node **geobot.admin**

##### Command: `geobot get role initiate`

Displays the role for users that need to contribute.

This command will behave the same as **tribute role get**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

##### Command: `geobot get role member`

Displays the roles for users to include in inactivity reports.

This checks the server level role permission overrides for node **geobot.user**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

##### Command: `geobot get role mod`

Displays server level role permission overrides for node **geobot.mod**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Keyword | Aliases
--------|--------
mod | moderator

##### Command: `geobot get role admin`

Displays server level role permission overrides for node **geobot.admin**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Keyword | Aliases
--------|--------
admin | administrator

#### Command: `geobot get timeout`

Geobot style `get timeout` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot get timeout contribution](#command-geobot-get-timeout-contribution) | displays the timeout for users that need to contribute
[geobot get timeout lastmessage](#command-geobot-get-timeout-lastmessage) | displays the user inactivity threshold for when they last posted a message in this Discord server
[geobot get timeout lastonline](#command-geobot-get-timeout-lastonline) | displays the user inactivity threshold for when they were last online on Discord

##### Command: `geobot get timeout contribution`

Displays the timeout for users that need to contribute.

This command will behave the same as **tribute timeout get**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.mod**.

Keyword | Aliases
--------|--------
contribution | tribute<br>donation

##### Command: `geobot get timeout lastmessage`

Displays the user inactivity threshold for when they last posted a message in this Discord server.

This command will behave the same as **inactivity get message**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.mod**.

Keyword | Aliases
--------|--------
lastmessage | last-message<br>message

##### Command: `geobot get timeout lastonline`

Displays the user inactivity threshold for when they were last online on Discord.

This command will behave the same as **inactivity get online**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.mod**.

Keyword | Aliases
--------|--------
lastonline | last-online<br>online

### Command: `geobot set`

Geobot style `set` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot set contribution](#command-geobot-set-contribution) | confirms user contributions
[geobot set game](#command-geobot-set-game) | Geobot style `get game` command emulation
[geobot set member](#command-geobot-set-member) | Geobot style `set member` command emulation
[geobot set timeout](#command-geobot-set-timeout) | Geobot style `set timeout` command emulation

#### Command: `geobot set contribution`

Confirms user contributions.

This command will behave the same as **tribute set**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.mod**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
contribution | tribute<br>donation
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

#### Command: `geobot set game`

Geobot style `get game` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot set game timeout](#command-geobot-set-game-timeout) | configures a user game inactivity threshold

##### Command: `geobot set game timeout`

Configures a user game inactivity threshold.

This command will behave the same as **inactivity set playing**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
--game \<game\> | Refers to a specific game with a matching id or name.
\[--duration\] \<days\> | The number of days after which a user is considered inactive. The flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
--game | //game
--duration | //duration<br>--days<br>//days<br>-d<br>/d

#### Command: `geobot set member`

Geobot style `set member` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot set member joined](#command-geobot-set-member-joined) | configures the timestamp from which the tribute timeout is checked
[geobot set member lastmessage](#command-geobot-set-member-lastmessage) | configures user activity information for when they last posted a message in this Discord server
[geobot set member lastonline](#command-geobot-set-member-lastonline) | configures user activity information for when they were last online on Discord
[geobot set member lastplayed](#command-geobot-set-member-lastplayed) | configures user game activity information

##### Command: `geobot set member joined`

Configures the timestamp from which the tribute timeout is checked.

This command will behave the same as **tribute start set**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--time \<timestamp\> | A valid ISO 8601 UTC timestamp (e.g. `1999-12-31T23:59:59.999`).<br>`now` can be used as a shortcut for the current date and time.<br>Relative input such as `5 days ago` or `-24h` can also be used.

Keyword | Aliases
--------|--------
joined | start<br>tracked
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--time | //time<br>--timestamp<br>//timestamp<br>-t<br>/t

##### Command: `geobot set member lastmessage`

Configures user activity information for when they last posted a message in this Discord server.

This command will behave the same as **activity set message**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--time \<timestamp\> | A valid ISO 8601 UTC timestamp (e.g. `1999-12-31T23:59:59.999`).<br>`now` can be used as a shortcut for the current date and time.<br>Relative input such as `5 days ago` or `-24h` can also be used.

Keyword | Aliases
--------|--------
lastmessage | last-message<br>message
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--time | //time<br>--timestamp<br>//timestamp<br>-t<br>/t

##### Command: `geobot set member lastonline`

Configures user activity information for when they were last online on Discord.

This command will behave the same as **activity set online**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--time \<timestamp\> | A valid ISO 8601 UTC timestamp (e.g. `1999-12-31T23:59:59.999`).<br>`now` can be used as a shortcut for the current date and time.<br>Relative input such as `5 days ago` or `-24h` can also be used.

Keyword | Aliases
--------|--------
lastonline | last-online<br>online
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--time | //time<br>--timestamp<br>//timestamp<br>-t<br>/t

##### Command: `geobot set member lastplayed`

Configures user game activity information.

This command will behave the same as **activity set playing**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--game \<game\> | Refers to a specific game with a matching id or name.
--time \<timestamp\> | A valid ISO 8601 UTC timestamp (e.g. `1999-12-31T23:59:59.999`).<br>`now` can be used as a shortcut for the current date and time.<br>Relative input such as `5 days ago` or `-24h` can also be used.

Keyword | Aliases
--------|--------
lastplayed | last-played<br>playing<br>played
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--game | //game
--time | //time<br>--timestamp<br>//timestamp<br>-t<br>/t

#### Command: `geobot set timeout`

Geobot style `set timeout` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot set timeout contribution](#command-geobot-set-timeout-contribution) | configures the timeout for users that need to contribute
[geobot set timeout lastmessage](#command-geobot-set-timeout-lastmessage) | configures the user inactivity threshold for when they last posted a message in this Discord server
[geobot set timeout lastonline](#command-geobot-set-timeout-lastonline) | configures the user inactivity threshold for when they were last online on Discord

##### Command: `geobot set timeout contribution`

Configures the timeout for users that need to contribute.

This command will behave the same as **tribute timeout set**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--duration\] \<days\> | The number of days in which a user must contribute. The flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
contribution | tribute<br>donation
--duration | //duration<br>--days<br>//days<br>-d<br>/d

##### Command: `geobot set timeout lastmessage`

Configures the user inactivity threshold for when they last posted a message in this Discord server.

This command will behave the same as **inactivity set message**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--duration\] \<days\> | The number of days after which a user is considered inactive. The flag is optional.

Keyword | Aliases
--------|--------
lastmessage | last-message<br>message
--duration | //duration<br>--days<br>//days<br>-d<br>/d

##### Command: `geobot set timeout lastonline`

Configures the user inactivity threshold for when they were last online on Discord.

This command will behave the same as **inactivity set online**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--duration\] \<days\> | The number of days after which a user is considered inactive. The flag is optional.

Keyword | Aliases
--------|--------
lastonline | last-online<br>online
--duration | //duration<br>--days<br>//days<br>-d<br>/d

### Command: `geobot unset`

Geobot style `unset` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot unset contribution](#command-geobot-unset-contribution) | revokes confirmation of user contributions
[geobot unset game](#command-geobot-unset-game) | Geobot style `get game` command emulation
[geobot unset member](#command-geobot-unset-member) | Geobot style `unset member` command emulation
[geobot unset timeout](#command-geobot-unset-timeout) | Geobot style `unset timeout` command emulation

#### Command: `geobot unset contribution`

Revokes confirmation of user contributions.

This command will behave the same as **tribute unset**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
contribution | tribute<br>donation
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

#### Command: `geobot unset game`

Geobot style `get game` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot unset game timeout](#command-geobot-unset-game-timeout) | removes a user game inactivity threshold

##### Command: `geobot unset game timeout`

Removes a user game inactivity threshold.

This command will behave the same as **inactivity unset playing**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--game\] \<game\> | Refers to a specific game with a matching id or name.<br>The `--game` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
--game | //game

#### Command: `geobot unset member`

Geobot style `unset member` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot unset member joined](#command-geobot-unset-member-joined) | clears the timestamp from which the tribute timeout is checked
[geobot unset member lastmessage](#command-geobot-unset-member-lastmessage) | configures user activity information for when they last posted a message in this Discord server
[geobot unset member lastonline](#command-geobot-unset-member-lastonline) | configures user activity information for when they were last online on Discord
[geobot unset member lastplayed](#command-geobot-unset-member-lastplayed) | configures user game activity information

##### Command: `geobot unset member joined`

Clears the timestamp from which the tribute timeout is checked.

This command will behave the same as **tribute start unset**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
joined | start<br>tracked
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

##### Command: `geobot unset member lastmessage`

Configures user activity information for when they last posted a message in this Discord server.

This command will behave the same as **activity unset message**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--time \<timestamp\> | A valid ISO 8601 UTC timestamp (e.g. `1999-12-31T23:59:59.999`).<br>`now` can be used as a shortcut for the current date and time.<br>Relative input such as `5 days ago` or `-24h` can also be used.

Keyword | Aliases
--------|--------
lastmessage | last-message<br>message
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--time | //time<br>--timestamp<br>//timestamp<br>-t<br>/t

##### Command: `geobot unset member lastonline`

Configures user activity information for when they were last online on Discord.

This command will behave the same as **activity unset online**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
lastonline | last-online<br>online
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

##### Command: `geobot unset member lastplayed`

Configures user game activity information.

This command will behave the same as **activity unset playing**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
--game \<game\> | Refers to a specific game with a matching id or name.

Keyword | Aliases
--------|--------
lastplayed | last-played<br>playing<br>played
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--game | //game

#### Command: `geobot unset timeout`

Geobot style `unset timeout` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot unset timeout contribution](#command-geobot-unset-timeout-contribution) | removes the configured timeout for users that need to contribute
[geobot unset timeout lastmessage](#command-geobot-unset-timeout-lastmessage) | removes the user inactivity threshold for when they last posted a message in this Discord server
[geobot unset timeout lastonline](#command-geobot-unset-timeout-lastonline) | removes the user inactivity threshold for when they were last online on Discord

##### Command: `geobot unset timeout contribution`

Removes the configured timeout for users that need to contribute.

This command will behave the same as **tribute timeout unset**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Keyword | Aliases
--------|--------
contribution | tribute<br>donation

##### Command: `geobot unset timeout lastmessage`

Removes the user inactivity threshold for when they last posted a message in this Discord server.

This command will behave the same as **inactivity unset message**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Keyword | Aliases
--------|--------
lastmessage | last-message<br>message

##### Command: `geobot unset timeout lastonline`

Removes the user inactivity threshold for when they were last online on Discord.

This command will behave the same as **inactivity unset online**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Keyword | Aliases
--------|--------
lastonline | last-online<br>online

### Command: `geobot add`

Geobot style `add` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot add role](#command-geobot-add-role) | Geobot style `add role` command emulation

#### Command: `geobot add role`

Geobot style `add role` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot add role initiate](#command-geobot-add-role-initiate) | configures the role for users that need to contribute
[geobot add role member](#command-geobot-add-role-member) | configures the roles for users to include in inactivity reports
[geobot add role mod](#command-geobot-add-role-mod) | configures server level role permission overrides for node **geobot.mod**
[geobot add role admin](#command-geobot-add-role-admin) | configures server level role permission overrides for node **geobot.admin**

##### Command: `geobot add role initiate`

Configures the role for users that need to contribute.

This command will behave the same as **tribute role set**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used or this option is provided first.

Keyword | Aliases
--------|--------
--role | //role<br>-r<br>/r

##### Command: `geobot add role member`

Configures the roles for users to include in inactivity reports.

This will affect the server level role permission overrides for node **geobot.user**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used or this option is provided first.

Keyword | Aliases
--------|--------
--role | //role<br>-r<br>/r

##### Command: `geobot add role mod`

Configures server level role permission overrides for node **geobot.mod**.

This command also configures server level role permission overrides for the following nodes:<br>
* **guild.activity.get**<br>
* **guild.inactivity.get**<br>
* **guild.tribute.get**<br>
* **guild.tribute.set**<br>
* **guild.tribute.start.get**<br>
* **guild.tribute.timeout.get**

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used or this option is provided first.

Keyword | Aliases
--------|--------
mod | moderator
--role | //role<br>-r<br>/r

##### Command: `geobot add role admin`

Configures server level role permission overrides for node **geobot.admin**.

This command also configures server level role permission overrides for the following nodes:<br>
* **guild.activity.get**<br>
* **guild.activity.set**<br>
* **guild.inactivity.get**<br>
* **guild.inactivity.set**<br>
* **guild.tribute.get**<br>
* **guild.tribute.set**<br>
* **guild.tribute.role.get**<br>
* **guild.tribute.role.set**<br>
* **guild.tribute.start.get**<br>
* **guild.tribute.start.set**<br>
* **guild.tribute.timeout.get**<br>
* **guild.tribute.timeout.set**

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used or this option is provided first.

Keyword | Aliases
--------|--------
admin | administrator
--role | //role<br>-r<br>/r

### Command: `geobot remove`

Geobot style `remove` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot remove role](#command-geobot-remove-role) | Geobot style `remove role` command emulation

#### Command: `geobot remove role`

Geobot style `remove role` command emulation.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[geobot remove role initiate](#command-geobot-remove-role-initiate) | removes the configured role for users that need to contribute
[geobot remove role member](#command-geobot-remove-role-member) | configures the roles for users to include in inactivity reports
[geobot remove role mod](#command-geobot-remove-role-mod) | configures server level role permission overrides for node **geobot.mod**
[geobot remove role admin](#command-geobot-remove-role-admin) | configures server level role permission overrides for node **geobot.admin**

##### Command: `geobot remove role initiate`

Removes the configured role for users that need to contribute.

This command will behave the same as **tribute role unset**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

##### Command: `geobot remove role member`

Configures the roles for users to include in inactivity reports.

This will affect the server level role permission overrides for node **geobot.user**.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used or this option is provided first.

Keyword | Aliases
--------|--------
--role | //role<br>-r<br>/r

##### Command: `geobot remove role mod`

Configures server level role permission overrides for node **geobot.mod**.

This command also configures server level role permission overrides for the following nodes:<br>
* **guild.activity.get**<br>
* **guild.inactivity.get**<br>
* **guild.tribute.get**<br>
* **guild.tribute.set**<br>
* **guild.tribute.start.get**<br>
* **guild.tribute.timeout.get**

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used or this option is provided first.

Keyword | Aliases
--------|--------
mod | moderator
--role | //role<br>-r<br>/r

##### Command: `geobot remove role admin`

Configures server level role permission overrides for node **geobot.admin**.

This command also configures server level role permission overrides for the following nodes:<br>
* **guild.activity.get**<br>
* **guild.activity.set**<br>
* **guild.inactivity.get**<br>
* **guild.inactivity.set**<br>
* **guild.tribute.get**<br>
* **guild.tribute.set**<br>
* **guild.tribute.role.get**<br>
* **guild.tribute.role.set**<br>
* **guild.tribute.start.get**<br>
* **guild.tribute.start.set**<br>
* **guild.tribute.timeout.get**<br>
* **guild.tribute.timeout.set**

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **geobot.admin**.

Option | Description
-------|------------
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used or this option is provided first.

Keyword | Aliases
--------|--------
admin | administrator
--role | //role<br>-r<br>/r
