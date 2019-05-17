package de.nevini.modules.legacy.contribution;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;
import de.nevini.resolvers.Resolvers;
import net.dv8tion.jda.core.entities.Member;

public class SetContributionCommand extends Command {

    public SetContributionCommand() {
        super(CommandDescriptor.builder()
                .keyword("contribution")
                .node(Node.LEGACY_SET_CONTRIBUTION)
                .description("confirms that a user has made a contribution")
                .syntax("<user>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrInput(event, member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        event.getLegacyContributionService().setContribution(member, true);
        event.reply(CommandReaction.OK, "Set contribution for **" + member.getEffectiveName() + "**.", event::complete);
    }

}
