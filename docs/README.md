# Nevini docs

This folder contains the documentation for each Nevini module.

## Commands

Each module comes with its own set of commands.
The description of each command will employ the following formatting.

Syntax           | Description 
-----------------|------------
keyword          | Identifies which command is executed and must be provided exactly as shown.
\[keyword\]      | Optional keywords can be omitted for brevity.<br>Do not type the brackets \[ \].
\<argument\>     | Arguments refer to what kind of value should be provided, for example a *user* reference.<br>Do not type the angles \< \>.
\[\<argument\>\] | Optional arguments can be omitted, but may cause the command to behave differently.<br>Do not type the brackets or angles \[\< \>\].

## Options

Options are arguments that are denoted by flags such as `--user` or `-u`.
They typically provide the scope for a command by providing one of the following types of reference.
If the provided reference is ambiguous, Nevini will prompt the user to use a unique reference.
Generally, it is advised to use IDs or mentions whenever possible.

The following table describes some common options used in commands.
Please refer to the documentation of each individual command to see which options are supported and what effect they have.

Syntax                        | Description
------------------------------|------------
\[--channel\] \[\<channel\>\] | Refers to a specific text channel using a channel mention, id or name.<br>The `--channel` flag is optional if a channel mention is used.<br>Refers to the current channel if only the `--channel` flag is provided.
--feed \<type\>               | Refers to a specific feed type.
--game \<game\>               | Refers to a specific game. You can provide (part of) the name of a game or its application ID.
--module \<module\>           | Refers to a specific bot module.
--node \<node\>               | Refers to a specific permission node for bot commands.
--permission \<permission\>   | Refers to a specific permission (e.g. "Manage Server").
\[--role\] \<role\>           | Refers to a specific role using a role mention, id or name.<br>The `--role` flag is optional if a role mention is used.
\[--user\] \[\<user\>\]       | Refers to a specific user using a mention, id or name.<br>The `--user` flag is optional if a user mention is used.<br>Refers to the current user if only the `--user` flag is provided.

## Global options

There are a few options that work with every command.

Option | Description
-------|------------
--dm   | Forces Nevini to reply via direct message instead of replying in the current channel.
--rm   | Nevini will attempt to remove the message containing the command after processing it.

## Aliases

Many commands or keywords can be expressed in multiple ways.
Below are some common aliases or synonyms you can use in many commands.
Each command may define its own alternative syntax in case it deviates from those listed here.

Keyword      | Aliases
-------------|--------
get          | display<br>echo<br>list<br>print<br>show
--channel    | //channel<br>-c<br>/c
--feed       | //feed<br>-f<br>/f
--game       | //game
--node       | //node<br>-n<br>/n
--permission | //permission<br>--perm<br>//perm<br>-p<br>/p
--role       | //role<br>-r<br>/r
--server     | //server<br>-s<br>/s<br>--guild<br>//guild<br>-g<br>/g
--user       | //user<br>-u<br>/u<br>--member<br>//member<br>-m<br>/m
