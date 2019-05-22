package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.modules.Node;
import de.nevini.resolvers.Resolvers;
import de.nevini.resolvers.StringResolver;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.util.List;

public class FindUserCommand extends Command {

    private static final StringResolver RESOLVER = new StringResolver("name or ID", "name");

    public FindUserCommand() {
        super(CommandDescriptor.builder()
                .keyword("find")
                .aliases(new String[]{"search"})
                .node(Node.GUILD_FIND_USER)
                .description("finds users by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("[--name] <name>")
                                .description("Any part of any name or the ID of users to look for. The flag is optional if this option is provided first.")
                                .keyword("--name")
                                .aliases(new String[]{"//name"})
                                .build()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        RESOLVER.resolveArgumentOrOptionOrInput(event, name -> acceptName(event, name));
    }

    private void acceptName(CommandEvent event, String name) {
        List<Member> members = Resolvers.MEMBER.findSorted(event, name);
        if (members.isEmpty()) {
            event.reply("I could not find any users that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            members.forEach(member -> embed.addField(
                    member.getEffectiveName(),
                    member.getUser().getId() + '\n' + member.getUser().getAsTag(),
                    true));
            event.reply(embed, event::complete);
        }
    }

}
