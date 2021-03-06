package de.nevini.modules.guild.tribute.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
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
                    + Formatter.formatInteger(delay) + " days.", event::complete);
        }
    }

}
