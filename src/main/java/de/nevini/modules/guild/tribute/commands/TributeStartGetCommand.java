package de.nevini.modules.guild.tribute.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.entities.Member;

public class TributeStartGetCommand extends Command {

    public TributeStartGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.GUILD_TRIBUTE_START_GET)
                .description("displays the timestamp from which the tribute timeout is checked")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrInput(event, member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        Long start = event.getTributeService().getStart(member);
        if (start == null) {
            event.reply("**" + member.getEffectiveName() + "** is not being tracked.", event::complete);
        } else {
            event.reply("**" + member.getEffectiveName() + "** is being tracked since "
                    + Formatter.formatTimestamp(start) + ".", event::complete);
        }
    }

}
