[![Discord Bots](https://top.gg/api/widget/status/570972920692736002.svg)](https://top.gg/bot/570972920692736002)
[![Discord Bots](https://top.gg/api/widget/servers/570972920692736002.svg)](https://top.gg/bot/570972920692736002)

# Nevini

Nevini is an open-source multi-purpose Discord bot.
It is hosted in Germany by [Geotim](https://github.com/geotim90), who is also the owner of the [GitHub repository](https://github.com/geotim90/Nevini).
Feel free to make suggestions in the [Issues](https://github.com/geotim90/Nevini/issues) section or even submit you own pull requests.
The code is licensed under [Apache License Version 2.0](#license).

## Invite

You can add Nevini to your Discord server by inviting the bot using the link below.
You need to be the owner of the Discord server or have the "Administrator" or "Manage Server" permission in order to do this.
You can grant or restrict permissions for Nevini as you wish.
Nevini will default to direct messages if everything else fails.

[https://discord.com/oauth2/authorize?scope=bot&client_id=570972920692736002&permissions=355392](https://discord.com/oauth2/authorize?scope=bot&client_id=570972920692736002&permissions=355392)

## Commands

To execute commands, write a message that begins with `NVN>` or `@Nevini` in a channel where Nevini can read messages.
You can change the `NVN>` prefix using the `prefix set` command.
The `@Nevini` mention can be changed by setting a nickname for the bot on your server.
Use the `help` command to get a list of commands available on your server.
Use the `help --all` command to get a list of all supported commands.

## Modules

Nevini has multiple modules, which can be activated separately.
By default, only the `core`, `guild` and `util` modules are active.
Other modules can be activated with the `module add` command.
Each module has its own documentation in the docs folder of the repository.
A more accessible version of the documentation is available under [nevini.de](https://nevini.de/docs).

## Permissions

Nevini uses its own permission system that is based on how the Discord permission system works.
For more details, check the core module documentation in the docs folder.
Server owners and administrators are not restricted by permissions.

## Support

For additional help and support, join [Geozone](https://discord.gg/Tw3WEvP) and feel free to ask any questions in the
support channel there.

## Privacy

Nevini uses the following gateway intents.

Intent | Purpose
------ | -------
`GUILDS` | to know which servers the bot is in
`GUILD_MEMBERS` | to know which users are in a server
`GUILD_VOICE_STATES` | to know when a user joins and leaves a voice channel for auto-roles
`GUILD_PRESENCES` | to know when a user is online and when a user is playing a game
`GUILD_MESSAGES` | to see messages in server channels
`GUILD_MESSAGE_REACTIONS` | to see reactions in server channels
`DIRECT_MESSAGES` | to see message in direct messages to the bot
`DIRECT_MESSAGE_REACTIONS` | to see reactions in direct messages to the bot

All interactions with the bot are logged. These logs are discarded after 90 days.

Nevini uses a database to persist certain information.

Information | Purpose
----------- | -------
Discord user activity | record of when a user was last online, last sent a message, and last played a game
Game metadata | persistent cache of application IDs, names and icon URLs
Nevini configuration | record of settings and parameters configured via commands
Nevini data | record of data entered via commands
osu! beatmap metadata | persistent cache to reduce the number of osu!api calls
osu! score metadata | persistent cache to reduce the number of osu!api calls
osu! user metadata | persistent cache to reduce the number of osu!api calls 

Information about Discord users can only be retrieved in servers that user is currently part of.

## License

Copyright &copy; 2019-2020 [Geotim](https://github.com/geotim90)

Licensed under the Apache License, Version 2.0 (the "License"); you may not use files in this repository except in
compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
