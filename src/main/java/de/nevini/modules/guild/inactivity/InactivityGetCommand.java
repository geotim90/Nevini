package de.nevini.modules.guild.inactivity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;

class InactivityGetCommand extends Command {

    InactivityGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .node(Node.GUILD_ACTIVITY_GET)
                .description("displays user inactivity thresholds")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder builder = event.createEmbedBuilder();
        Integer onlineThreshold = event.getInactivityService().getOnlineThreshold(event.getGuild());
        builder.addField("Discord",
                onlineThreshold == null ? "(not configured)" : onlineThreshold + " days", true);
        Integer messageThreshold = event.getInactivityService().getMessageThreshold(event.getGuild());
        builder.addField(event.getGuild().getName(),
                messageThreshold == null ? "(not configured)" : messageThreshold + " days", true);
        event.getInactivityService().getPlayingThresholds(event.getGuild()).forEach((game, days) ->
                builder.addField(event.getGameService().getGameName(game), days + " days", true));
        event.reply(builder, event::complete);
    }

}
