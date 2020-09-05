package de.nevini.modules.guild.activity.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.modules.admin.game.data.GameData;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.entities.Member;

public class ActivityGetPlayingCommand extends Command {

    public ActivityGetPlayingCommand() {
        super(CommandDescriptor.builder()
                .keyword("playing")
                .aliases(new String[]{"played", "last-played", "lastplayed"})
                .node(Node.GUILD_ACTIVITY_GET)
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
