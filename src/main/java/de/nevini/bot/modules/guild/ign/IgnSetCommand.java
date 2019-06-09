package de.nevini.bot.modules.guild.ign;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.db.game.GameData;
import de.nevini.bot.resolvers.StringResolver;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandOptionDescriptor;
import de.nevini.framework.command.CommandReaction;
import net.dv8tion.jda.core.entities.Member;

class IgnSetCommand extends Command {

    private static final StringResolver nameResolver = new StringResolver("in-game name", "name",
            CommandOptionDescriptor.builder()
                    .syntax("[--name] <name>")
                    .description("The in-game name to use. The flag is optional if this option is provided first.")
                    .keyword("--name")
                    .aliases(new String[]{"//name"})
                    .build());

    IgnSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .node(Node.GUILD_IGN_SET)
                .description("configures the in-game name for a specific user in a specific game")
                .options(new CommandOptionDescriptor[]{
                        nameResolver.describe(),
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
        nameResolver.resolveArgumentOrOptionOrInput(event, name -> acceptName(event, member, game, name));
    }

    private void acceptName(CommandEvent event, Member member, GameData game, String name) {
        if (event.getMember().canInteract(member)) {
            event.getIgnService().setIgn(member, game, name);
            event.reply(CommandReaction.OK, event::complete);
        } else {
            event.reply(CommandReaction.PROHIBITED, "You can only configure in-game names "
                    + "for users whose highest role is lower than your highest role!", event::complete);
        }
    }

}
