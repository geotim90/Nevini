package de.nevini.bot.modules.guild.find;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandOptionDescriptor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class FindChannelCommand extends Command {

    public FindChannelCommand() {
        super(CommandDescriptor.builder()
                .keyword("channel")
                .aliases(new String[]{"c"})
                .node(Node.GUILD_FIND_CHANNEL)
                .description("finds text channels by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.CHANNEL.describe(true, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.CHANNEL.resolveListArgumentOrOptionOrInput(event, channels -> acceptChannels(event, channels));
    }

    private void acceptChannels(CommandEvent event, List<TextChannel> channels) {
        if (channels.isEmpty()) {
            event.reply("I could not find any channels that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            channels.forEach(channel -> embed.addField(channel.getName(), channel.getId(), true));
            event.reply(embed, event::complete);
        }
    }

}
