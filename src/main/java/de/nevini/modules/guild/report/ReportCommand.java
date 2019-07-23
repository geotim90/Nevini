package de.nevini.modules.guild.report;

import de.nevini.command.*;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.Value;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ReportCommand extends Command {

    private static final Pattern SERVER_FLAG = Pattern.compile("(?i)(?:(?:--|//)(?:server|guild)|[-/][sg])");

    public ReportCommand() {
        super(CommandDescriptor.builder()
                .keyword("report")
                .node(Node.GUILD_REPORT_SELF)
                .description("displays an activity report for the entire server or just a single user")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        Resolvers.ROLE.describe(),
                        CommandOptionDescriptor.builder()
                                .syntax("--server")
                                .description("Displays an activity report for the entire server.")
                                .keyword("--server")
                                .aliases(new String[]{"//server", "--guild", "//guild", "-s", "/s", "-g", "/g"})
                                .build()
                })
                .details("By default, you need the **Manage Server** permission to execute this command with `--user`.\n"
                        + "Permission overrides for `--user` may be applied on node **guild.report.user**.\n\n"
                        + "By default, you need the **Manage Server** permission to execute this command with `--role`.\n"
                        + "Permission overrides for `--role` may be applied on node **guild.report.role**.\n\n"
                        + "By default, you need the **Manage Server** permission to execute this command with `--server`.\n"
                        + "Permission overrides for `--server` may be applied on node **guild.report.server**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getOptions().getOptions().stream().map(SERVER_FLAG::matcher).anyMatch(Matcher::matches)
                && checkUserNodePermission(event, Node.GUILD_REPORT_SERVER)
        ) {
            doGuildReport(event, null);
        } else if (!event.getOptions().getArguments().isEmpty()
                && (checkUserNodePermission(event, Node.GUILD_REPORT_USER)
                || checkUserNodePermission(event, Node.GUILD_REPORT_ROLE))
        ) {
            if (checkUserNodePermission(event, Node.GUILD_REPORT_USER)) {
                Resolvers.MEMBER.resolveArgumentOrOptionOrDefaultIfExists(
                        event, event.getMember(), member -> {
                            if (member != null) doMemberReport(event, member);
                        }
                );
            }
            if (checkUserNodePermission(event, Node.GUILD_REPORT_ROLE)) {
                Resolvers.ROLE.resolveOptionOrInputIfExists(
                        event, role -> {
                            if (role != null) doGuildReport(event, role);
                        }
                );
            }
        } else {
            doMemberReport(event, event.getMember());
        }
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

        StringBuilder builder = new StringBuilder("Activity report for **" + member.getEffectiveName() + "**\n\n");

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

    private void doGuildReport(CommandEvent event, Role role) {
        Integer onlineThreshold = event.getInactivityService().getOnlineThreshold(event.getGuild());
        Integer messageThreshold = event.getInactivityService().getMessageThreshold(event.getGuild());
        Map<Long, Integer> playingThresholds = event.getInactivityService().getPlayingThresholds(event.getGuild());

        long onlineTimeout = onlineThreshold == null ? 0
                : OffsetDateTime.now(ZoneOffset.UTC).minusDays(onlineThreshold).toInstant().toEpochMilli();
        long messageTimeout = messageThreshold == null ? 0
                : OffsetDateTime.now(ZoneOffset.UTC).minusDays(messageThreshold).toInstant().toEpochMilli();

        Map<Member, GuildReportEntry> reportEntries = new HashMap<>();

        List<Member> members = role == null
                ? event.getGuild().getMembers()
                : event.getGuild().getMembersWithRoles(role);

        for (Member member : members) {
            if (!member.getUser().isBot()) {
                Long activityOnline = event.getActivityService().getActivityOnline(member);
                Long activityMessage = event.getActivityService().getActivityMessage(member);
                Map<Long, Long> activityPlaying = event.getActivityService().getActivityPlaying(member);

                if (onlineThreshold != null) {
                    if (activityOnline == null) {
                        // no "online" data
                        merge(reportEntries, new GuildReportEntry(member, 0,
                                CommandReaction.WARNING.getUnicode() + " **" + member.getEffectiveName()
                                        + "** was last online **unknown**\n"));
                    } else if (activityOnline < onlineTimeout) {
                        // "online" inactivity
                        merge(reportEntries, new GuildReportEntry(member, onlineTimeout - activityOnline,
                                CommandReaction.ERROR.getUnicode() + " **" + member.getEffectiveName()
                                        + "** was last online **" + Formatter.formatLargestUnitAgo(activityOnline)
                                        + "** (" + Formatter.formatTimestamp(activityOnline) + ")\n"));
                    }
                }

                if (messageThreshold != null) {
                    if (activityMessage == null) {
                        // no "message" data
                        merge(reportEntries, new GuildReportEntry(member, 0,
                                CommandReaction.WARNING.getUnicode() + " **" + member.getEffectiveName()
                                        + "** last messaged **unknown**\n"
                        ));
                    } else if (activityMessage < messageTimeout) {
                        // "message" inactivity
                        merge(reportEntries, new GuildReportEntry(member, messageTimeout - activityMessage,
                                CommandReaction.ERROR.getUnicode() + " **" + member.getEffectiveName()
                                        + "** last messaged **" + Formatter.formatLargestUnitAgo(activityMessage)
                                        + "** (" + Formatter.formatTimestamp(activityMessage) + ")\n"));
                    }
                }

                playingThresholds.forEach((gameId, gameThreshold) -> {
                    String gameName = event.getGameService().getGameName(gameId);
                    Long gameTimeout = OffsetDateTime.now(ZoneOffset.UTC).minusDays(gameThreshold).toInstant().toEpochMilli();
                    Long activityGame = activityPlaying.get(gameId);
                    if (activityGame == null) {
                        // no "playing" data
                        merge(reportEntries, new GuildReportEntry(member, 0,
                                CommandReaction.WARNING.getUnicode() + " **" + member.getEffectiveName()
                                        + "** last played **" + gameName + "** (" + Long.toUnsignedString(gameId)
                                        + ") **unknown**\n"));
                    } else if (activityGame < gameTimeout) {
                        merge(reportEntries, new GuildReportEntry(member, gameTimeout - activityGame,
                                CommandReaction.ERROR.getUnicode() + " **" + member.getEffectiveName()
                                        + "** last played **" + gameName + "** (" + Long.toUnsignedString(gameId) + ") **"
                                        + Formatter.formatLargestUnitAgo(activityGame) + "** ("
                                        + Formatter.formatTimestamp(activityGame) + ")\n"
                        ));
                    }
                });
            }
        }

        if (reportEntries.isEmpty()) {
            event.reply("No inactivity to report for **" + (role == null ? event.getGuild().getName()
                    : role.getName()) + "**", event::complete);
        } else {
            StringBuilder builder = new StringBuilder("Activity report for **" + (role == null
                    ? event.getGuild().getName() : role.getName()) + "**\n\n");
            reportEntries.values().stream().sorted(Comparator.comparingLong(GuildReportEntry::getOvertime).reversed()
                    .thenComparing(e -> e.getMember().getEffectiveName())).forEach(e -> builder.append(e.getText()));
            event.reply(builder.toString(), event::complete);
        }
    }

    private void merge(Map<Member, GuildReportEntry> reportEntries, GuildReportEntry entry) {
        reportEntries.merge(entry.getMember(), entry, (a, b) -> b.getOvertime() > a.getOvertime() ? b : a);
    }

    @Value
    private static class GuildReportEntry {
        private final Member member;
        private final long overtime;
        private final String text;
    }

}
