package de.nevini.modules.guild.inactivity.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.modules.admin.game.data.GameData;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;

public class InactivityGetPlayingCommand extends Command {

    public InactivityGetPlayingCommand() {
        super(CommandDescriptor.builder()
                .keyword("playing")
                .aliases(new String[]{"played", "last-played", "lastplayed"})
                .node(Node.GUILD_INACTIVITY_GET)
                .description("displays user game inactivity thresholds")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.GAME.describe()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.GAME.resolveArgumentOrOptionOrInputIfExists(event, game -> acceptGame(event, game));
    }

    private void acceptGame(CommandEvent event, GameData game) {
        if (game == null) {
            EmbedBuilder builder = event.createGuildEmbedBuilder();
            event.getInactivityService().getPlayingThresholds(event.getGuild()).forEach((g, days) ->
                    builder.addField(event.getGameService().getGameName(g), days + " days", true));
            event.reply(builder, event::complete);
        } else {
            Integer days = event.getInactivityService().getPlayingThreshold(event.getGuild(), game);
            if (days == null || days < 1) {
                event.reply("No playing timeout has been configured for **" + game.getName() + "**.", event::complete);
            } else {
                event.reply("The configured playing timeout for **" + game.getName() + "** is **" + days.toString()
                        + " days**", event::complete);
            }
        }
    }

}
