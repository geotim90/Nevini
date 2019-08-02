package de.nevini.modules.guild.tribute;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.entities.Member;

class TributeGetCommand extends Command {

    TributeGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .node(Node.GUILD_TRIBUTE_GET)
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .description("displays user contributions")
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
        boolean tribute = event.getTributeService().getTribute(member);
        if (tribute) {
            event.reply("**" + member.getEffectiveName() + "** has contributed.", event::complete);
        } else {
            event.reply("**" + member.getEffectiveName() + "** has **not** contributed.", event::complete);
        }
    }

}
