package de.nevini.modules.util.find;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

class FindUserCommand extends Command {

    FindUserCommand() {
        super(CommandDescriptor.builder()
                .keyword("user")
                .aliases(new String[]{"users", "member", "members", "u", "m"})
                .node(Node.UTIL_FIND_USER)
                .description("finds users by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(true, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveListArgumentOrOptionOrInput(event, members -> acceptMembers(event, members));
    }

    private void acceptMembers(CommandEvent event, List<Member> members) {
        if (members.isEmpty()) {
            event.reply("I could not find any users that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createGuildEmbedBuilder();
            members.forEach(member -> embed.addField(
                    member.getEffectiveName(),
                    member.getUser().getId() + '\n' + member.getUser().getAsTag(),
                    true));
            event.reply(embed, event::complete);
        }
    }

}
