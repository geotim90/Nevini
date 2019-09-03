package de.nevini.modules.guild.tribute;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Member;

public class TributeDelaySetCommand extends Command {

    public TributeDelaySetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .node(Node.GUILD_TRIBUTE_DELAY_SET)
                .description("configures delayed tribute timeouts for individual users")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        Resolvers.DURATION.describe()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrInput(event, member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        Resolvers.DURATION.resolveOptionOrInput(event, duration -> acceptMemberDuration(event, member, duration));
    }

    private void acceptMemberDuration(CommandEvent event, Member member, Long duration) {
        event.getTributeService().setDelay(member, duration);
        event.reply(CommandReaction.OK, event::complete);
    }

}
