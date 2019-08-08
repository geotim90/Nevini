package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.jpa.game.GameData;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Member;

import java.time.OffsetDateTime;

public class ActivitySetPlayingCommand extends Command {

    public ActivitySetPlayingCommand() {
        super(CommandDescriptor.builder()
                .keyword("playing")
                .aliases(new String[]{"played", "last-played", "lastplayed"})
                .node(Node.GUILD_ACTIVITY_SET)
                .description("configures user game activity information")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        Resolvers.GAME.describe(),
                        Resolvers.TIMESTAMP.describe()
                })
                .details("Note that timestamps provided via this command do not override *real* activity information. "
                        + "Instead, they just provided a manual minimum value for activity reports on this server.")
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
        Resolvers.TIMESTAMP.resolveOptionOrInput(event,
                timestamp -> acceptMemberGameTimestamp(event, member, game, timestamp));
    }

    private void acceptMemberGameTimestamp(CommandEvent event, Member member, GameData game, OffsetDateTime timestamp) {
        // validate timestamp
        if (timestamp.isAfter(OffsetDateTime.now())) {
            event.reply(CommandReaction.WARNING, "You cannot use a timestamp in future!", event::complete);
            return;
        }

        // set activity
        event.getActivityService().manualActivityPlaying(member, game, timestamp);
        event.reply(CommandReaction.OK, event::complete);
    }

}
