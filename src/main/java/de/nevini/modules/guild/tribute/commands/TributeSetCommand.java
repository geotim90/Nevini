package de.nevini.modules.guild.tribute.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Member;

public class TributeSetCommand extends Command {

    public TributeSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .aliases(new String[]{"confirm"})
                .node(Node.GUILD_TRIBUTE_SET)
                .description("confirms user contributions")
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
        event.getTributeService().setTribute(member);
        event.reply(CommandReaction.OK, event::complete);
    }

}
