package de.nevini.bot.modules.guild.find;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandOptionDescriptor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.util.List;

class FindUserCommand extends Command {

    FindUserCommand() {
        super(CommandDescriptor.builder()
                .keyword("user")
                .aliases(new String[]{"member", "u", "m"})
                .node(Node.GUILD_FIND_USER)
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
            EmbedBuilder embed = event.createEmbedBuilder();
            members.forEach(member -> embed.addField(
                    member.getEffectiveName(),
                    member.getUser().getId() + '\n' + member.getUser().getAsTag(),
                    true));
            event.reply(embed, event::complete);
        }
    }

}
