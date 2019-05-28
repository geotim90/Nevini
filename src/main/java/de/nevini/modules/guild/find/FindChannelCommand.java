package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.common.ChannelResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
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
                        ChannelResolver.describe().build()
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
