package de.nevini.modules.guild.tribute;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.entities.Member;

public class TributeDelayGetCommand extends Command {

    public TributeDelayGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.GUILD_TRIBUTE_DELAY_GET)
                .description("displays delayed tribute timeouts for individual users")
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
        Long delay = event.getTributeService().getDelay(member);
        if (delay == null) {
            event.reply("**" + member.getEffectiveName() + "**'s tribute timeout has not been delayed.",
                    event::complete);
        } else {
            event.reply("**" + member.getEffectiveName() + "**'s tribute timeout has been delayed for "
                    + Formatter.formatLong(delay) + " days.", event::complete);
        }
    }

}
