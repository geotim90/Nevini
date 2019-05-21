package de.nevini.modules.guild.ign;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.db.ign.IgnData;
import de.nevini.modules.Node;
import de.nevini.resolvers.StringResolver;
import de.nevini.util.Finder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class IgnFindCommand extends Command {

    private final StringResolver resolver = new StringResolver("name", "name");

    public IgnFindCommand() {
        super(CommandDescriptor.builder()
                .keyword("find")
                .aliases(new String[]{"search"})
                .node(Node.GUILD_IGN_GET)
                .description("finds users by any of their names")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("[--name] <name>")
                                .description("Any part of the names of users to look for. The flag is optional.")
                                .keyword("--name")
                                .aliases(new String[]{"//name"})
                                .build()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        resolver.resolveArgumentOrOptionOrInput(event, name -> acceptName(event, name));
    }

    private void acceptName(CommandEvent event, String name) {
        List<IgnData> ignMatches = Finder.find(
                event.getIgnService().findByIgn(event.getGuild(), name),
                IgnData::getName,
                name);
        if (!ignMatches.isEmpty()) {
            EmbedBuilder builder = event.createEmbedBuilder();
            for (IgnData ign : ignMatches) {
                Member member = event.getGuild().getMemberById(ign.getUser());
                if (member != null) builder.addField(member.getEffectiveName(), ign.getName(), true);
            }
            event.reply(builder, event::complete);
        } else {
            List<Member> discordMatches = FinderUtil.findMembers(name, event.getGuild()).stream()
                    .filter(m -> !m.getUser().isBot())
                    .sorted(Comparator.comparing(Member::getEffectiveName))
                    .collect(Collectors.toList());
            if (discordMatches.isEmpty()) {
                event.reply("I could not find any users that matched your query.");
            } else {
                EmbedBuilder builder = event.createEmbedBuilder();
                for (Member member : discordMatches) {
                    builder.addField(member.getEffectiveName(), member.getUser().getName(), true);
                }
                event.reply(builder, event::complete);
            }
        }
    }

}
