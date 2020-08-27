package de.nevini.modules.util.find;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

class FindChannelCommand extends Command {

    FindChannelCommand() {
        super(CommandDescriptor.builder()
                .keyword("channel")
                .aliases(new String[]{"channels", "c"})
                .node(Node.UTIL_FIND_CHANNEL)
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
            EmbedBuilder embed = event.createGuildEmbedBuilder();
            channels.forEach(channel -> embed.addField(channel.getName(), channel.getId(), true));
            event.reply(embed, event::complete);
        }
    }

}
