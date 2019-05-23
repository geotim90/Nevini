package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.modules.Node;
import de.nevini.resolvers.StringResolver;
import de.nevini.resolvers.common.Resolvers;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class FindChannelCommand extends Command {

    private static final StringResolver nameResolver = new StringResolver("name or ID", "name");

    public FindChannelCommand() {
        super(CommandDescriptor.builder()
                .keyword("channel")
                .aliases(new String[]{"c"})
                .node(Node.GUILD_FIND_CHANNEL)
                .description("finds text channels by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("[--name] <name>")
                                .description("Any part of the name or ID of text channels to look for. The flag is optional if this option is provided first.")
                                .keyword("--name")
                                .aliases(new String[]{"//name"})
                                .build()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        nameResolver.resolveArgumentOrOptionOrInput(event, name -> acceptName(event, name));
    }

    private void acceptName(CommandEvent event, String name) {
        List<TextChannel> channels = Resolvers.CHANNEL.findSorted(event, name);
        if (channels.isEmpty()) {
            event.reply("I could not find any channels that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            channels.forEach(channel -> embed.addField(channel.getName(), channel.getId(), true));
            event.reply(embed, event::complete);
        }
    }

}
