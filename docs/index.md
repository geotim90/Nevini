# Nevini

Nevini is an open-source multi-purpose Discord bot.
Its primary instance is hosted by [Geotim](https://github.com/geotim90), who is also the owner of the [GitHub repository](https://github.com/geotim90/Nevini).
Feel free to make suggestions in the [Issues](https://github.com/geotim90/Nevini/issues) section or even submit you own pull requests.
The code is licensed under [Apache License Version 2.0](#license).

## Invite

You can add Nevini to your Discord server by inviting the bot using the link below.
You need to be the owner of the Discord server or have the "Administrator" or "Manage Server" permission in order to do this.
You can grant or restrict permissions for Nevini as you wish.
Nevini will default to direct messages if everything else fails.

[https://discordapp.com/oauth2/authorize?client_id=570972920692736002&scope=bot&permissions=519232](https://discordapp.com/oauth2/authorize?client_id=570972920692736002&scope=bot&permissions=519232).

## Commands

To execute commands, write a message that begins with `>` or `@Nevini` in a channel where Nevini can read messages.
You can change the `>` prefix using the `prefix set` command.
The `@Nevini` mention can be changed by setting a nickname for the bot.
Use the `help` command to get a list of commands available on your server.
Use the `help --all` command to get a list of all supported commands.

## Modules

Nevini has multiple modules, which can be activated seperately.
By default, only the `core` and `guild` modules are active.
Other modules can be activated with the `module add` command.
Each module has its own documentation in the [docs folder](./docs) of the repository.
A more accessible version of the documentation is available under [nevini.de](https://nevini.de/docs).

## Permissions

Nevini uses its own permission system that is based on how the Discord permission system works.
For more details, check the core module documentation in the docs folder.
Server owners and administrators are not restricted by permissions.

## Support

For additional help and support, join [Geozone](https://discord.gg/jKVZFhD) and feel free to ask any questions in the
support channel there.

## License

Copyright &copy; 2019 [Geotim](https://github.com/geotim90)

Licensed under the Apache License, Version 2.0 (the "License"); you may not use files in this repository except in
compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
