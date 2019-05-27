package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.StringResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.util.List;

public class FindUserCommand extends Command {

    private static final StringResolver nameResolver = new StringResolver("name or ID", "name");

    public FindUserCommand() {
        super(CommandDescriptor.builder()
                .keyword("user")
                .aliases(new String[]{"member", "u", "m"})
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
        nameResolver.resolveArgumentOrOptionOrInput(event, name -> acceptName(event, name));
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
