package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.jpa.game.GameData;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.entities.Member;

public class ActivityGetPlayingCommand extends Command {

    public ActivityGetPlayingCommand() {
        super(CommandDescriptor.builder()
                .keyword("playing")
                .aliases(new String[]{"played", "last-played", "lastplayed"})
                .node(Node.GUILD_ACTIVITY_SET)
                .description("displays user game activity information")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        Resolvers.GAME.describe()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrInput(event, member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        Resolvers.GAME.resolveOptionOrInput(event, game -> acceptMemberGame(event, member, game));
    }

    private void acceptMemberGame(CommandEvent event, Member member, GameData game) {
        Long lastPlayed = event.getActivityService().getActivityPlaying(member, game.getId());
        if (lastPlayed == null) {
            event.reply(member.getEffectiveName() + " has not played this game recently.", event::complete);
        } else {
            event.reply("**" + member.getEffectiveName() + "** last played **" + game.getName() + "** ("
                    + Long.toUnsignedString(game.getId()) + ") **" + Formatter.formatLargestUnitAgo(lastPlayed) + "** ("
                    + Formatter.formatTimestamp(lastPlayed) + ")\n", event::complete);
        }
    }

}
