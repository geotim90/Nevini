# Module: `util`

These commands provide various utility functions. This modules is active by default but can be deactivated.

Command | Description
--------|------------
[debug](#command-debug) | various commands for debugging and analysis
[deobfuscate](#command-deobfuscate) | deobfuscates text
[find](#command-find) | finds users, roles etc. by any of their identifiers
[html-escape](#command-html-escape) | converts text using HTML escape sequences
[html-unescape](#command-html-unescape) | converts text using HTML escape sequences
[lower-case](#command-lower-case) | converts text to lower case
[morse-decode](#command-morse-decode) | converts morse code to text
[morse-encode](#command-morse-encode) | converts text to morse code
[obfuscate](#command-obfuscate) | obfuscates text
[reverse](#command-reverse) | reverses text
[small-caps](#command-small-caps) | converts text to lower case
[upper-case](#command-upper-case) | converts text to upper case
[url-decode](#command-url-decode) | converts text using URL escape sequences
[url-encode](#command-url-encode) | converts text using URL escape sequences

## Command: `debug`

Various commands for debugging and analysis.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[debug permission](#command-debug-permission) | creates a data dump of all relevant permissions and overrides on the server

### Command: `debug permission`

Creates a data dump of all relevant permissions and overrides on the server.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **util.debug.permission**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a role mention is used.
--server | Refers to the current server.

Keyword | Aliases
--------|--------
permission | permissions<br>perm<br>perms
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--role | //role<br>-r<br>/r
--server | //server<br>--guild<br>//guild<br>-s<br>/s<br>-g<br>/g

## Command: `deobfuscate`

Deobfuscates text.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **util.unicode.obfuscate**.

## Command: `find`

Finds users, roles etc. by any of their identifiers.

By default, this command will behave the same as **find user**.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
find | search<br>resolve

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

Permission overrides may be applied on node **util.find.user**.

Option | Description
-------|------------
\[--user\] \[\<user\>\] | Refers to all users with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used or this option is provided first.<br>Refers to the current user if only the `--user` flag is provided.

Keyword | Aliases
--------|--------
user | users<br>member<br>members<br>u<br>m
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m

### Command: `find role`

Finds roles by any of their identifiers.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **util.find.role**.

Option | Description
-------|------------
\[--role\] \<role\> | Refers to all roles with a matching mention, id or name.<br>The `--role` flag is optional if a role mention is used or this option is provided first.

Keyword | Aliases
--------|--------
role | roles<br>r
--role | //role<br>-r<br>/r

### Command: `find permission`

Finds permission by any of their identifiers.

This command can be executed in a text channel or via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **util.find.permission**.

Option | Description
-------|------------
\[--permission\] \<permission\> | Refers to all permissions with a matching name (e.g. "Manage Server").<br>The `--permission` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
permission | permissions<br>perm<br>perms<br>p
--permission | //permission<br>--perm<br>//perm<br>-p<br>/p

### Command: `find node`

Finds nodes by any of their identifiers.

This command can be executed in a text channel or via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **util.find.module**.

Option | Description
-------|------------
\[--node\] \<node\> | Refers to all permission nodes for bot commands with a matching name.<br>The `--node` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
node | nodes<br>n
--node | //node<br>-n<br>/n

### Command: `find module`

Finds modules by any of their identifiers.

This command can be executed in a text channel or via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **util.find.module**.

Option | Description
-------|------------
\[--module\] \<module\> | Refers to all bot modules with a matching name.<br>The `--module` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
module | modules
--module | //module

### Command: `find game`

Finds games by any of their identifiers.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **util.find.game**.

Option | Description
-------|------------
\[--game\] \<game\> | Refers to all games with a matching id or name.<br>The `--game` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
game | games
--game | //game

### Command: `find channel`

Finds text channels by any of their identifiers.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **util.find.channel**.

Option | Description
-------|------------
\[--channel\] \[\<channel\>\] | Refers to all text channels with a matching mention, id or name.<br>The `--channel` flag is optional if a channel mention is used or this option is provided first.<br>Refers to the current channel if only the `--channel` flag is provided.

Keyword | Aliases
--------|--------
channel | channels<br>c
--channel | //channel<br>-c<br>/c

## Command: `html-escape`

Converts text using HTML escape sequences.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **util.unicode.html**.

Keyword | Aliases
--------|--------
html-escape | htmlescape<br>html-esc<br>htmlesc

## Command: `html-unescape`

Converts text using HTML escape sequences.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **util.unicode.html**.

Keyword | Aliases
--------|--------
html-unescape | htmlunescape<br>html-unesc<br>htmlunesc

## Command: `lower-case`

Converts text to lower case.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **util.unicode.text**.

Keyword | Aliases
--------|--------
lower-case | lowercase<br>lower

## Command: `morse-decode`

Converts morse code to text.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **util.unicode.morse**.

Keyword | Aliases
--------|--------
morse-decode | morsedecode<br>morse-dec<br>morsedec

## Command: `morse-encode`

Converts text to morse code.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **util.unicode.morse**.

Keyword | Aliases
--------|--------
morse-encode | morseencode<br>morse-enc<br>morseenc

## Command: `obfuscate`

Obfuscates text.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **util.unicode.obfuscate**.

## Command: `reverse`

Reverses text.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **util.unicode.text**.

## Command: `small-caps`

Converts text to lower case.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **util.unicode.text**.

Keyword | Aliases
--------|--------
small-caps | smallcaps<br>sc

## Command: `upper-case`

Converts text to upper case.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **util.unicode.text**.

Keyword | Aliases
--------|--------
upper-case | uppercase<br>upper

## Command: `url-decode`

Converts text using URL escape sequences.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **util.unicode.url**.

Keyword | Aliases
--------|--------
url-decode | urldecode<br>url-dec<br>urldec

## Command: `url-encode`

Converts text using URL escape sequences.

This command can be executed in a text channel or via direct message.

Permission overrides may be applied on node **util.unicode.url**.

Keyword | Aliases
--------|--------
url-encode | urlencode<br>url-enc<br>urlenc
