# Module: `core`

These commands are a core part of Nevini and are always available. In other words, this module is active by default and cannot be deactivated.

Command                           | Description
----------------------------------|------------
[help](#command-help)             | provides a list of commands or details on a specific command
[module](#command-module)         | displays and configures modules
[permission](#command-permission) | displays and configures permission node overrides for bot commands
[ping](#command-ping)             | measures the bot's communication latency
[prefix](#command-prefix)         | displays and configures the command prefix

# Command: `help`

Command               | Description
----------------------|------------
[help](#command-help) | provides a list of commands or details on a specific command

Option          | Description
----------------|------------
\[\<command\>\] | the specific command to look up

If a valid command is provided, this will display details on that specific command.<br>
If no valid command is provided, this will display a list of commands.

Permission overrides may be applied on node **core.help**.

# Command: `module`

Command                                         | Description
------------------------------------------------|------------
[module](#command-module)                       | displays and configures modules
[module get](#command-module-get)               | displays a list of modules
[module activate](#command-module-activate)     | activates a module
[module deactivate](#command-module-deactivate) | deactivates a module

By default, this command will behave the same as [module get](#command-module-get).

Keyword | Aliases
--------|--------
module  | modules
