package de.nevini.modules.guild.ign;

import de.nevini.command.*;
import de.nevini.db.game.GameData;
import de.nevini.resolvers.StringResolver;
import de.nevini.resolvers.common.GameResolver;
import de.nevini.resolvers.common.MemberResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.entities.Member;

public class IgnSetCommand extends Command {

    private static final StringResolver nameResolver = new StringResolver("in-game name", "name");

    public IgnSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .node(Node.GUILD_IGN_SET)
                .description("configures the in-game name for a specific user in a specific game")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("[--name] <name>")
                                .description("The in-game name to use. The flag is optional if this option is provided first.")
                                .keyword("--name")
                                .aliases(new String[]{"//name"})
                                .build(),
                        MemberResolver.describe().build(),
                        GameResolver.describe().build()
                })
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
        event.getIgnService().setIgn(member, game, name);
        event.reply(CommandReaction.OK, event::complete);
    }

}
