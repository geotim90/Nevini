package de.nevini.modules.legacy.contribution;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;
import de.nevini.resolvers.Resolvers;
import net.dv8tion.jda.core.entities.Member;

public class GetContributionCommand extends Command {

    public GetContributionCommand() {
        super(CommandDescriptor.builder()
                .keyword("contribution")
                .node(Node.LEGACY_GET_CONTRIBUTION)
                .description("displays whether or not a user has made a contribution")
                .syntax("<user>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrInput(event, member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        boolean contribution = event.getLegacyContributionService().getContribution(member);
        if (contribution) {
            event.reply("**" + member.getEffectiveName() + "** has made a contribution \uD83C\uDF89", event::complete);
        } else {
            event.reply("**" + member.getEffectiveName() + "** has **not** made a contribution \uD83D\uDE22", event::complete);
        }
    }

}
