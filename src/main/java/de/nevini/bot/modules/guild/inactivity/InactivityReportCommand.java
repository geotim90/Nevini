package de.nevini.bot.modules.guild.inactivity;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import de.nevini.bot.util.Formatter;
import de.nevini.framework.command.CommandOptionDescriptor;
import de.nevini.framework.command.CommandReaction;
import net.dv8tion.jda.core.entities.Member;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

class InactivityReportCommand extends Command {

    InactivityReportCommand() {
        super(CommandDescriptor.builder()
                .keyword("report")
                .children(new Command[]{
                        // TODO implement // new InactivityReportOnlineCommand(),
                        // TODO implement // new InactivityReportMessageCommand(),
                        // TODO implement // new InactivityReportPlayingCommand(),
                })
                .node(Node.GUILD_INACTIVITY_REPORT)
                .description("displays an inactivity report for the entire server or just a single user")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrDefaultIfExists(
                event, event.getMember(), member -> acceptMember(event, member)
        );
    }

    private void acceptMember(CommandEvent event, Member member) {
        if (member == null) {
            doGuildReport(event);
        } else {
            doMemberReport(event, member);
        }
    }

    private void doGuildReport(CommandEvent event) {
        // TODO implement
    }

    private void doMemberReport(CommandEvent event, Member member) {
        Integer onlineThreshold = event.getInactivityService().getOnlineThreshold(event.getGuild());
        Integer messageThreshold = event.getInactivityService().getMessageThreshold(event.getGuild());
        Map<Long, Integer> playingThresholds = event.getInactivityService().getPlayingThresholds(event.getGuild());

        long onlineTimeout = onlineThreshold == null ? 0
                : OffsetDateTime.now(ZoneOffset.UTC).minusDays(onlineThreshold).toInstant().toEpochMilli();
        long messageTimeout = messageThreshold == null ? 0
                : OffsetDateTime.now(ZoneOffset.UTC).minusDays(messageThreshold).toInstant().toEpochMilli();

        Long activityOnline = event.getActivityService().getActivityOnline(member);
        Long activityMessage = event.getActivityService().getActivityMessage(member);
        Map<Long, Long> activityPlaying = event.getActivityService().getActivityPlaying(member);

        StringBuilder builder = new StringBuilder("Inactivity report for **" + member.getEffectiveName() + "**\n\n");

        if (activityOnline == null) {
            // no "online" data
            builder.append(onlineThreshold == null ? CommandReaction.DEFAULT_NOK.getUnicode()
                    : CommandReaction.WARNING.getUnicode()).append(" Last online: **unknown**\n");
        } else {
            if (activityOnline > onlineTimeout) {
                // recent "online" activity
                builder.append(onlineThreshold == null ? CommandReaction.DEFAULT_OK.getUnicode()
                        : CommandReaction.OK.getUnicode());
            } else {
                // "online" inactivity
                builder.append(CommandReaction.ERROR.getUnicode());
            }
            builder.append(" Last online: **").append(Formatter.formatLargestUnitAgo(activityOnline))
                    .append("** (").append(Formatter.formatTimestamp(activityOnline)).append(")\n");
        }

        if (activityMessage == null) {
            // no "message" data
            builder.append(messageThreshold == null ? CommandReaction.DEFAULT_NOK.getUnicode()
                    : CommandReaction.WARNING.getUnicode()).append(" Last message: **unknown**\n");
        } else {
            if (activityMessage > messageTimeout) {
                // recent "message" activity
                builder.append(messageThreshold == null ? CommandReaction.DEFAULT_OK.getUnicode()
                        : CommandReaction.OK.getUnicode());
            } else {
                // "message" inactivity
                builder.append(CommandReaction.ERROR.getUnicode());
            }
            builder.append(" Last message: **").append(Formatter.formatLargestUnitAgo(activityMessage))
                    .append("** (").append(Formatter.formatTimestamp(activityMessage)).append(")\n");
        }

        playingThresholds.forEach((gameId, gameThreshold) -> {
            String gameName = event.getGameService().getGameName(gameId);
            Long gameTimeout = OffsetDateTime.now(ZoneOffset.UTC).minusDays(gameThreshold).toInstant().toEpochMilli();
            Long activityGame = activityPlaying.get(gameId);
            if (activityGame == null) {
                // no "playing" data
                builder.append(CommandReaction.WARNING.getUnicode()).append(" Last played: **")
                        .append(gameName).append("** (").append(Long.toUnsignedString(gameId))
                        .append("): **unknown**\n");
            } else {
                builder.append(activityGame > gameTimeout ? CommandReaction.OK.getUnicode()
                        : CommandReaction.ERROR.getUnicode()).append(" Last played **").append(gameName).append("** (")
                        .append(Long.toUnsignedString(gameId)).append("): **")
                        .append(Formatter.formatLargestUnitAgo(activityGame)).append("** (")
                        .append(Formatter.formatTimestamp(activityGame)).append(")\n");
            }
        });

        event.reply(builder.toString(), event::complete);
    }

}
