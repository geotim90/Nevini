package de.nevini.modules.guild.ign.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.modules.admin.game.data.GameData;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Member;

class IgnUnsetCommand extends Command {

    IgnUnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .aliases(new String[]{"remove", "clear", "reset"})
                .node(Node.GUILD_IGN_SET)
                .description("removes the in-game name for a specific user in a specific game")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(),
                        Resolvers.GAME.describe()
                })
                .details("Users can only configure in-game names "
                        + "for users whose highest role is lower than their highest role.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveOptionOrDefault(event, event.getMember(), member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        Resolvers.GAME.resolveOptionOrInput(event, game -> acceptGame(event, member, game));
    }

    private void acceptGame(CommandEvent event, Member member, GameData game) {
        if (event.getMember().canInteract(member)) {
            event.getIgnService().unsetIgn(member, game);
            event.reply(CommandReaction.OK, event::complete);
        } else {
            event.reply(CommandReaction.PROHIBITED, "You can only configure in-game names "
                    + "for users whose highest role is lower than your highest role!", event::complete);
        }
    }

}
