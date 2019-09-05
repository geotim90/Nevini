package de.nevini.modules.guild.tribute;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.Data;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class TributeReportCommand extends Command {

    TributeReportCommand() {
        super(CommandDescriptor.builder()
                .keyword("report")
                .node(Node.GUILD_REPORT_SELF)
                .description("displays a tribute report for the entire server or just a single user")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        Resolvers.ROLE.describe(),
                        Resolvers.GUILD_FLAG.describe()
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
        if ((StringUtils.isNotEmpty(event.getArgument()) || Resolvers.MEMBER.isOptionPresent(event))
                && checkUserNodePermission(event, Node.GUILD_REPORT_USER)
        ) {
            Resolvers.MEMBER.resolveArgumentOrOptionOrDefaultIfExists(
                    event, event.getMember(), member -> {
                        if (member != null) doMemberReport(event, member);
                    }
            );
        } else if (Resolvers.ROLE.isOptionPresent(event) && checkUserNodePermission(event, Node.GUILD_REPORT_ROLE)) {
            Resolvers.ROLE.resolveOptionOrInputIfExists(
                    event, role -> {
                        if (role != null) doGuildReport(event, role);
                    }
            );
        } else if (Resolvers.GUILD_FLAG.isOptionPresent(event)
                && checkUserNodePermission(event, Node.GUILD_REPORT_SERVER)
        ) {
            doGuildReport(event, event.getTributeService().getRole(event.getGuild()));
        } else {
            doMemberReport(event, event.getMember());
        }
    }

    private void doMemberReport(CommandEvent event, Member member) {
        Long joined = event.getTributeService().getStart(member);
        boolean hasTributeRole = hasTributeRole(event, member);
        Long contributionTimeoutInDays = event.getTributeService().getTimeout(event.getGuild());
        boolean contribution = event.getTributeService().getTribute(member);
        if (hasTributeRole && contributionTimeoutInDays != null) {
            long contributionDelayInDays = ObjectUtils.defaultIfNull(
                    event.getTributeService().getDelay(member), 0L);
            OffsetDateTime deadline = joined == null ? OffsetDateTime.MAX
                    : OffsetDateTime.ofInstant(Instant.ofEpochMilli(joined), ZoneOffset.UTC)
                    .plusDays(contributionTimeoutInDays + contributionDelayInDays);
            OffsetDateTime now = OffsetDateTime.now();
            if (contribution) {
                if (deadline.isAfter(now)) {
                    event.reply(CommandReaction.DEFAULT_OK, "**" + member.getEffectiveName()
                            + "** has contributed with **" + Formatter.formatLargestUnitBetween(now, deadline)
                            + "** remaining (" + Formatter.formatTimestamp(deadline) + ").", event::complete);
                } else {
                    event.reply(CommandReaction.OK, "**" + member.getEffectiveName()
                            + "** has contributed - the deadline was **"
                            + Formatter.formatLargestUnitBetween(now, deadline) + " ago** ("
                            + Formatter.formatTimestamp(deadline) + ").", event::complete);
                }
            } else if (deadline.isAfter(now)) {
                event.reply(CommandReaction.WARNING, "**" + member.getEffectiveName() + "** needs to contribute in **"
                                + Formatter.formatLargestUnitBetween(now, deadline) + "** ("
                                + Formatter.formatTimestamp(deadline) + ").",
                        event::complete);
            } else {
                event.reply(CommandReaction.ERROR, "**" + member.getEffectiveName() + "** should have contributed **"
                        + Formatter.formatLargestUnitBetween(now, deadline) + " ago** ("
                        + Formatter.formatTimestamp(deadline) + ").", event::complete);
            }
        } else {
            if (contribution) {
                event.reply(CommandReaction.DEFAULT_OK, "**" + member.getEffectiveName() + "** has contributed.",
                        event::complete);
            } else {
                event.reply(CommandReaction.DEFAULT_NOK, "**" + member.getEffectiveName() + "** has not contributed.",
                        event::complete);
            }
        }
    }

    @Data
    private static class MemberReportDetails {
        Member member;
        boolean hasTributeRole;
        Long joined;
        boolean contribution;
    }

    private void doGuildReport(CommandEvent event, Role role) {
        Long contributionTimeoutInDays = event.getTributeService().getTimeout(event.getGuild());

        // gather report details
        Collection<MemberReportDetails> reportDetails = event.getGuild().getMembers().stream().filter(
                member -> role == null || member.getRoles().contains(role)
        ).map(member -> {
            MemberReportDetails memberDetails = new MemberReportDetails();
            memberDetails.setMember(member);
            memberDetails.setHasTributeRole(hasTributeRole(event, member));
            memberDetails.setJoined(event.getTributeService().getStart(member));
            memberDetails.setContribution(event.getTributeService().getTribute(member));
            return memberDetails;
        }).collect(Collectors.toList());

        // build report
        StringBuilder builder = new StringBuilder();
        OffsetDateTime now = OffsetDateTime.now();

        if (contributionTimeoutInDays != null) {
            List<MemberReportDetails> contributors = reportDetails.stream()
                    .filter(member -> member.isHasTributeRole() && member.getJoined() != null && member.isContribution())
                    .sorted(Comparator.comparing(MemberReportDetails::getJoined))
                    .collect(Collectors.toList());
            if (!contributors.isEmpty()) {
                builder.append("\n__**Users that have made a contribution**__\n");
                for (MemberReportDetails e : contributors) {
                    long contributionDelayInDays = ObjectUtils.defaultIfNull(
                            event.getTributeService().getDelay(e.getMember()), 0L);
                    OffsetDateTime deadline = OffsetDateTime.ofInstant(Instant.ofEpochMilli(e.getJoined()),
                            ZoneOffset.UTC).plusDays(contributionTimeoutInDays + contributionDelayInDays);
                    if (deadline.isAfter(now)) {
                        builder.append(CommandReaction.DEFAULT_OK.getUnicode()).append(" **")
                                .append(e.getMember().getEffectiveName()).append("** has contributed with **")
                                .append(Formatter.formatLargestUnitBetween(now, deadline))
                                .append("** remaining (").append(Formatter.formatTimestamp(deadline)).append(")\n");
                    } else {
                        builder.append(CommandReaction.OK.getUnicode()).append(" **")
                                .append(e.getMember().getEffectiveName())
                                .append("** has contributed - the deadline was **")
                                .append(Formatter.formatLargestUnitBetween(deadline, now))
                                .append(" ago** (").append(Formatter.formatTimestamp(deadline)).append(")\n");
                    }
                }
            }

            List<MemberReportDetails> nonContributors = reportDetails.stream()
                    .filter(member -> member.isHasTributeRole() && member.getJoined() != null && !member.isContribution())
                    .sorted(Comparator.comparing(MemberReportDetails::getJoined))
                    .collect(Collectors.toList());
            if (!nonContributors.isEmpty()) {
                builder.append("\n__**Users that have not made a contribution**__\n");
                for (MemberReportDetails e : nonContributors) {
                    long contributionDelayInDays = ObjectUtils.defaultIfNull(
                            event.getTributeService().getDelay(e.getMember()), 0L);
                    OffsetDateTime deadline = OffsetDateTime.ofInstant(Instant.ofEpochMilli(e.getJoined()),
                            ZoneOffset.UTC).plusDays(contributionTimeoutInDays + contributionDelayInDays);
                    if (deadline.isAfter(now)) {
                        builder.append(CommandReaction.WARNING.getUnicode()).append(" **")
                                .append(e.getMember().getEffectiveName())
                                .append("** needs to contribute in **")
                                .append(Formatter.formatLargestUnitBetween(now, deadline))
                                .append("** (").append(Formatter.formatTimestamp(deadline)).append(")\n");
                    } else {
                        builder.append(CommandReaction.ERROR.getUnicode()).append(" **")
                                .append(e.getMember().getEffectiveName())
                                .append("** should have contributed **")
                                .append(Formatter.formatLargestUnitBetween(deadline, now))
                                .append(" ago** (").append(Formatter.formatTimestamp(deadline)).append(")\n");
                    }
                }
            }
        }

        if (builder.length() == 0) {
            event.reply("Nothing to report.", event::complete);
        } else {
            event.reply(builder.toString(), event::complete);
        }
    }

    private boolean hasTributeRole(CommandEvent event, Member member) {
        Role role = event.getTributeService().getRole(event.getGuild());
        return role != null && member.getRoles().contains(role);
    }

}
