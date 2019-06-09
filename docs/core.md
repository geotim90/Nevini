# Module: `core`

These commands are a core part of Nevini and are always available. In other words, this module is active by default and cannot be deactivated.

Command | Description
--------|------------
[help](#command-help) | provides a list of commands or details on a specific command
[module](#command-module) | displays and configures modules
[permission](#command-permission) | displays and configures permission node overrides for bot commands
[ping](#command-ping) | measures the bot's communication latency
[prefix](#command-prefix) | displays and configures the command prefix

## Command: `help`

Provides a list of commands or details on a specific command.

If a valid command is provided, this will display details on that specific command.<br>
If no valid command is provided, this will display a list of commands.

This command can be executed in a text or via direct message.

Permission overrides may be applied on node **core.help**.

Option | Description
-------|------------
\[\<command\>\] | the specific command to look up

Keyword | Aliases
--------|--------
help | about<br>info

## Command: `module`

Displays and configures modules.

By default, this command will behave the same as **module get**.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
module | modules

Command | Description
--------|------------
[module get](#command-module-get) | displays a list of modules
[module activate](#command-module-activate) | activates a module
[module deactivate](#command-module-deactivate) | deactivates a module

### Command: `module get`

Displays a list of modules.

If a module is provided, this will display a list of all matching modules.<br>
If no module is provided, this will display a list of all modules.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **core.module.get**.

Option | Description
-------|------------
\[--module\] \<module\> | Refers to all bot modules with a matching name.<br>The `--module` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
get | display<br>echo<br>list<br>print<br>show
--module | //module

### Command: `module activate`

Activates a module.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **core.module.activate**.

Option | Description
-------|------------
\[--module\] \<module\> | Refers to a specific bot module with a matching name.<br>The `--module` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
activate | add<br>enable<br>+
--module | //module

### Command: `module deactivate`

Deactivates a module.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **core.module.deactivate**.

Option | Description
-------|------------
\[--module\] \<module\> | Refers to a specific bot module with a matching name.<br>The `--module` flag is optional if this option is provided first.

Keyword | Aliases
--------|--------
deactivate | disable<br>remove<br>-
--module | //module

## Command: `permission`

Displays and configures permission node overrides for bot commands.

By default, this command will behave the same as **permission get**.

This command can only be executed in a text channel and not via direct message.

Keyword | Aliases
--------|--------
permission | permissions<br>perm<br>perms

Command | Description
--------|------------
[permission get](#command-permission-get) | displays effective permissions for bot commands
[permission debug](#command-permission-debug) | displays a permission node trace for bot commands
[permission allow](#command-permission-allow) | configures permission node overrides for bot commands
[permission deny](#command-permission-deny) | configures permission node overrides for bot commands
[permission reset](#command-permission-reset) | resets permission node overrides for bot commands

### Command: `permission get`

Displays effective permissions for bot commands.

Permission node overrides for bot commands are applied in the following order:<br>
1. Default permissions<br>
2. Server permissions<br>
3. Permissions based on effective permissions (e.g. "Manage Server")<br>
4. Role permissions<br>
5. User permissions<br>
6. Channel permissions<br>
7. Channel-specific permissions based on effective permissions (e.g. "Manage Server")<br>
8. Channel-specific role permissions<br>
9. Channel-specific user permissions

If multiple overrides on the same "level" disagree with each other (e.g. conflicting roles), **allow** will trump **deny**.<br>
Server owners and administrators are not restricted by permission node overrides.<br>
Users can only configure permissions for permission nodes they have themselves.<br>
Users can only configure permissions for roles of a lower position than their highest role.<br>
Users can only configure permissions for users whose highest role is lower than their highest role.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **core.permission.get**.

Option | Description
-------|------------
\[--node\] \<node\> | Refers to all permission nodes for bot commands with a matching name.<br>The `--node` flag is optional if this option is provided first.
--all | Explicitly refers to all permission nodes.
--server | Changes the scope to server-wide permissions instead of channel-specific permissions.
--permission \<permission\> | Refers to a specific permission with a matching name (e.g. "Manage Server").
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used.
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used.<br>Refers to the current user if only the `--user` flag is provided.
\[--channel\] \[\<channel\>\] | Refers to a specific text channel with a matching mention, id or name.<br>The `--channel` flag is optional if a channel mention is used.<br>Refers to the current channel if only the `--channel` flag is provided.

Keyword | Aliases
--------|--------
get | display<br>echo<br>list<br>print<br>show
--node | //node<br>-n<br>/n
--all | //all<br>-a<br>/a
--server | //server<br>--guild<br>//guild<br>-s<br>/s<br>-g<br>/g
--permission | //permission<br>--perm<br>//perm<br>-p<br>/p
--role | //role<br>-r<br>/r
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--channel | //channel<br>-c<br>/c

### Command: `permission debug`

Displays a permission node trace for bot commands.

Permission node overrides for bot commands are applied in the following order:<br>
1. Default permissions<br>
2. Server permissions<br>
3. Permissions based on effective permissions (e.g. "Manage Server")<br>
4. Role permissions<br>
5. User permissions<br>
6. Channel permissions<br>
7. Channel-specific permissions based on effective permissions (e.g. "Manage Server")<br>
8. Channel-specific role permissions<br>
9. Channel-specific user permissions

If multiple overrides on the same "level" disagree with each other (e.g. conflicting roles), **allow** will trump **deny**.<br>
Server owners and administrators are not restricted by permission node overrides.<br>
Users can only configure permissions for permission nodes they have themselves.<br>
Users can only configure permissions for roles of a lower position than their highest role.<br>
Users can only configure permissions for users whose highest role is lower than their highest role.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **core.permission.debug**.

Option | Description
-------|------------
\[--node\] \<node\> | Refers to a specific permission node for bot commands with a matching name.<br>The `--node` flag is optional if this option is provided first.
--all | Explicitly refers to all permission nodes.
--server | Changes the scope to server-wide permissions instead of channel-specific permissions.
--permission \<permission\> | Refers to a specific permission with a matching name (e.g. "Manage Server").
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used.
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used.<br>Refers to the current user if only the `--user` flag is provided.
\[--channel\] \[\<channel\>\] | Refers to a specific text channel with a matching mention, id or name.<br>The `--channel` flag is optional if a channel mention is used.<br>Refers to the current channel if only the `--channel` flag is provided.

Keyword | Aliases
--------|--------
--node | //node<br>-n<br>/n
--all | //all<br>-a<br>/a
--server | //server<br>--guild<br>//guild<br>-s<br>/s<br>-g<br>/g
--permission | //permission<br>--perm<br>//perm<br>-p<br>/p
--role | //role<br>-r<br>/r
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--channel | //channel<br>-c<br>/c

### Command: `permission allow`

Configures permission node overrides for bot commands.

Permission node overrides for bot commands are applied in the following order:<br>
1. Default permissions<br>
2. Server permissions<br>
3. Permissions based on effective permissions (e.g. "Manage Server")<br>
4. Role permissions<br>
5. User permissions<br>
6. Channel permissions<br>
7. Channel-specific permissions based on effective permissions (e.g. "Manage Server")<br>
8. Channel-specific role permissions<br>
9. Channel-specific user permissions

If multiple overrides on the same "level" disagree with each other (e.g. conflicting roles), **allow** will trump **deny**.<br>
Server owners and administrators are not restricted by permission node overrides.<br>
Users can only configure permissions for permission nodes they have themselves.<br>
Users can only configure permissions for roles of a lower position than their highest role.<br>
Users can only configure permissions for users whose highest role is lower than their highest role.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **core.permission.allow**.

Option | Description
-------|------------
\[--node\] \<node\> | Refers to all permission nodes for bot commands with a matching name.<br>The `--node` flag is optional if this option is provided first.
--all | Explicitly refers to all permission nodes.
--server | Changes the scope to server-wide permissions instead of channel-specific permissions.
--permission \<permission\> | Refers to a specific permission with a matching name (e.g. "Manage Server").
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used.
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used.<br>Refers to the current user if only the `--user` flag is provided.
\[--channel\] \[\<channel\>\] | Refers to a specific text channel with a matching mention, id or name.<br>The `--channel` flag is optional if a channel mention is used.<br>Refers to the current channel if only the `--channel` flag is provided.

Keyword | Aliases
--------|--------
allow | add<br>grant
--node | //node<br>-n<br>/n
--all | //all<br>-a<br>/a
--server | //server<br>--guild<br>//guild<br>-s<br>/s<br>-g<br>/g
--permission | //permission<br>--perm<br>//perm<br>-p<br>/p
--role | //role<br>-r<br>/r
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--channel | //channel<br>-c<br>/c

### Command: `permission deny`

Configures permission node overrides for bot commands.

Permission node overrides for bot commands are applied in the following order:<br>
1. Default permissions<br>
2. Server permissions<br>
3. Permissions based on effective permissions (e.g. "Manage Server")<br>
4. Role permissions<br>
5. User permissions<br>
6. Channel permissions<br>
7. Channel-specific permissions based on effective permissions (e.g. "Manage Server")<br>
8. Channel-specific role permissions<br>
9. Channel-specific user permissions

If multiple overrides on the same "level" disagree with each other (e.g. conflicting roles), **allow** will trump **deny**.<br>
Server owners and administrators are not restricted by permission node overrides.<br>
Users can only configure permissions for permission nodes they have themselves.<br>
Users can only configure permissions for roles of a lower position than their highest role.<br>
Users can only configure permissions for users whose highest role is lower than their highest role.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **core.permission.deny**.

Option | Description
-------|------------
\[--node\] \<node\> | Refers to all permission nodes for bot commands with a matching name.<br>The `--node` flag is optional if this option is provided first.
--all | Explicitly refers to all permission nodes.
--server | Changes the scope to server-wide permissions instead of channel-specific permissions.
--permission \<permission\> | Refers to a specific permission with a matching name (e.g. "Manage Server").
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used.
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used.<br>Refers to the current user if only the `--user` flag is provided.
\[--channel\] \[\<channel\>\] | Refers to a specific text channel with a matching mention, id or name.<br>The `--channel` flag is optional if a channel mention is used.<br>Refers to the current channel if only the `--channel` flag is provided.

Keyword | Aliases
--------|--------
deny | block<br>refuse
--node | //node<br>-n<br>/n
--all | //all<br>-a<br>/a
--server | //server<br>--guild<br>//guild<br>-s<br>/s<br>-g<br>/g
--permission | //permission<br>--perm<br>//perm<br>-p<br>/p
--role | //role<br>-r<br>/r
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--channel | //channel<br>-c<br>/c

### Command: `permission reset`

Resets permission node overrides for bot commands.

Permission node overrides for bot commands are applied in the following order:<br>
1. Default permissions<br>
2. Server permissions<br>
3. Permissions based on effective permissions (e.g. "Manage Server")<br>
4. Role permissions<br>
5. User permissions<br>
6. Channel permissions<br>
7. Channel-specific permissions based on effective permissions (e.g. "Manage Server")<br>
8. Channel-specific role permissions<br>
9. Channel-specific user permissions

If multiple overrides on the same "level" disagree with each other (e.g. conflicting roles), **allow** will trump **deny**.<br>
Server owners and administrators are not restricted by permission node overrides.<br>
Users can only configure permissions for permission nodes they have themselves.<br>
Users can only configure permissions for roles of a lower position than their highest role.<br>
Users can only configure permissions for users whose highest role is lower than their highest role.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Roles** permission to execute this command.<br>
Permission overrides may be applied on node **core.permission.reset**.

Option | Description
-------|------------
\[--node\] \<node\> | Refers to all permission nodes for bot commands with a matching name.<br>The `--node` flag is optional if this option is provided first.
--all | Explicitly refers to all permission nodes.
--server | Changes the scope to server-wide permissions instead of channel-specific permissions.
--permission \<permission\> | Refers to a specific permission with a matching name (e.g. "Manage Server").
\[--role\] \<role\> | Refers to a specific role with a matching mention, id or name.<br>The `--role` flag is optional if a channel mention is used.
\[--user\] \[\<user\>\] | Refers to a specific user with a matching mention, id, name, nickname or in-game name.<br>The `--user` flag is optional if a user mention is used.<br>Refers to the current user if only the `--user` flag is provided.
\[--channel\] \[\<channel\>\] | Refers to a specific text channel with a matching mention, id or name.<br>The `--channel` flag is optional if a channel mention is used.<br>Refers to the current channel if only the `--channel` flag is provided.

Keyword | Aliases
--------|--------
reset | clear<br>default
--node | //node<br>-n<br>/n
--all | //all<br>-a<br>/a
--server | //server<br>--guild<br>//guild<br>-s<br>/s<br>-g<br>/g
--permission | //permission<br>--perm<br>//perm<br>-p<br>/p
--role | //role<br>-r<br>/r
--user | //user<br>--member<br>//member<br>-u<br>/u<br>-m<br>/m
--channel | //channel<br>-c<br>/c

## Command: `ping`

Measures the bot's communication latency.

This command can be executed in a text or via direct message.

Permission overrides may be applied on node **core.ping**.

Keyword | Aliases
--------|--------
ping | pong<br>latency

## Command: `prefix`

Displays and configures the command prefix.

By default, this command will behave the same as **prefix get**.

This command can only be executed in a text channel and not via direct message.

Command | Description
--------|------------
[prefix get](#command-prefix-get) | displays the currently configured command prefix
[prefix set](#command-prefix-set) | configures the command prefix

### Command: `prefix get`

Displays the currently configured command prefix.

This command can only be executed in a text channel and not via direct message.

Permission overrides may be applied on node **core.prefix.get**.

Keyword | Aliases
--------|--------
get | display<br>echo<br>list<br>print<br>show

### Command: `prefix set`

Configures the command prefix.

The command prefix cannot be longer than 32 characters and must not contain spaces.

This command can only be executed in a text channel and not via direct message.

By default, you need the **Manage Server** permission to execute this command.<br>
Permission overrides may be applied on node **core.prefix.set**.

Option | Description
-------|------------
\[--prefix\] \<prefix\> | The command prefix to use. The flag is optional.

Keyword | Aliases
--------|--------
--prefix | //prefix
