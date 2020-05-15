package de.nevini.modules.geobot.report;

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

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

public class ReportCommand extends Command {

    public ReportCommand() {
        super(CommandDescriptor.builder()
                .keyword("report")
                .node(Node.GEOBOT_MOD)
                .description("displays an activity report for the entire server or just a single user")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrDefaultIfExists(event, event.getMember(),
                member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        if (member == null) {
            doGuildReport(event);
        } else {
            doMemberReport(event, member);
        }
    }

    private void doGuildReport(CommandEvent event) {
        OffsetDateTime now = OffsetDateTime.now();

        // get timeouts
        Long contributionTimeoutInDays = event.getTributeService().getTimeout(event.getGuild());
        Integer onlineTimeoutInDays = event.getInactivityService().getOnlineThreshold(event.getGuild());
        Integer messageTimeoutInDays = event.getInactivityService().getMessageThreshold(event.getGuild());
        Map<Long, Integer> playingTimeoutsInDays = event.getInactivityService().getPlayingThresholds(event.getGuild());

        // get inactivity thresholds
        Long onlineThreshold = onlineTimeoutInDays == null ? null
                : now.minusDays(onlineTimeoutInDays).toInstant().toEpochMilli();
        Long messageThreshold = messageTimeoutInDays == null ? null
                : now.minusDays(messageTimeoutInDays).toInstant().toEpochMilli();
        Map<Long, Long> playingThresholds = new HashMap<>();
        playingTimeoutsInDays.forEach((gameId, playingTimeoutInDays) -> playingThresholds.put(gameId,
                now.minusDays(playingTimeoutInDays).toInstant().toEpochMilli()));

        // gather report details
        Collection<MemberReportDetails> reportDetails = event.getGuild().getMembers().stream().filter(
                member -> hasInitiateRole(event, member) || hasMemberRole(event, member)
        ).map(member -> {
            MemberReportDetails memberDetails = new MemberReportDetails();
            memberDetails.setMember(member);
            memberDetails.setInitiate(hasInitiateRole(event, member));
            memberDetails.setJoined(event.getTributeService().getStart(member));
            memberDetails.setContribution(event.getTributeService().getTribute(member));
            memberDetails.setLastOnline(event.getActivityService().getActivityOnline(member));
            memberDetails.setLastMessage(event.getActivityService().getActivityMessage(member));
            playingTimeoutsInDays.keySet().forEach(gameId -> memberDetails.getLastPlayed().put(gameId,
                    event.getActivityService().getActivityPlaying(member, gameId)));

            // additional information for initiates
            if (memberDetails.isInitiate()) {
                long contributionDelayInDays = ObjectUtils.defaultIfNull(
                        event.getTributeService().getDelay(member), 0L);
                memberDetails.setDeadline(OffsetDateTime.ofInstant(Instant.ofEpochMilli(memberDetails.getJoined()),
                        ZoneOffset.UTC).plusDays(contributionTimeoutInDays + contributionDelayInDays));
            }

            // check inactivity thresholds
            if (onlineThreshold != null) {
                if (memberDetails.getLastOnline() == null || memberDetails.getLastOnline() < onlineThreshold) {
                    memberDetails.setAnyInactivity(true);
                } else {
                    memberDetails.setAnyActivity(true);
                }
            }
            if (messageThreshold != null) {
                if (memberDetails.getLastMessage() == null || memberDetails.getLastMessage() < messageThreshold) {
                    memberDetails.setAnyInactivity(true);
                } else {
                    memberDetails.setAnyActivity(true);
                }
            }
            playingThresholds.forEach((gameId, playingThreshold) -> {
                Long lastPlayed = memberDetails.getLastPlayed().get(gameId);
                if (lastPlayed == null || lastPlayed < playingThreshold) {
                    memberDetails.setAnyInactivity(true);
                } else {
                    memberDetails.setAnyActivity(true);
                }
            });
            return memberDetails;
        }).collect(Collectors.toList());

        // build report
        StringBuilder builder = new StringBuilder();

        if (contributionTimeoutInDays != null) {
            List<MemberReportDetails> contributors = reportDetails.stream()
                    .filter(member -> member.isInitiate() && member.getJoined() != null && member.isContribution())
                    .sorted(Comparator.comparing(MemberReportDetails::getDeadline))
                    .collect(Collectors.toList());
            if (!contributors.isEmpty()) {
                builder.append("\n__**Initiates that have made a contribution**__ - ")
                        .append(contributors.size()).append("\n");
                for (MemberReportDetails e : contributors) {
                    if (e.getDeadline().isAfter(now)) {
                        builder.append(CommandReaction.DEFAULT_OK.getUnicode()).append(" **")
                                .append(e.getMember().getEffectiveName())
                                .append("** has contributed and is due to be promoted in **")
                                .append(Formatter.formatLargestUnitBetween(now, e.getDeadline()))
                                .append("** (").append(Formatter.formatTimestamp(e.getDeadline())).append(")\n");
                    } else {
                        builder.append(CommandReaction.OK.getUnicode()).append(" **")
                                .append(e.getMember().getEffectiveName())
                                .append("** has contributed and was due to be promoted **")
                                .append(Formatter.formatLargestUnitBetween(e.getDeadline(), now))
                                .append(" ago** (").append(Formatter.formatTimestamp(e.getDeadline())).append(")\n");
                    }
                }
            }

            List<MemberReportDetails> nonContributors = reportDetails.stream()
                    .filter(member -> member.isInitiate() && member.getJoined() != null && !member.isContribution())
                    .sorted(Comparator.comparing(MemberReportDetails::getDeadline))
                    .collect(Collectors.toList());
            if (!nonContributors.isEmpty()) {
                builder.append("\n__**Initiates that have not made a contribution**__ - ")
                        .append(nonContributors.size()).append("\n");
                for (MemberReportDetails e : nonContributors) {
                    if (e.getDeadline().isAfter(now)) {
                        builder.append(CommandReaction.WARNING.getUnicode()).append(" **")
                                .append(e.getMember().getEffectiveName())
                                .append("** needs to contribute in **")
                                .append(Formatter.formatLargestUnitBetween(now, e.getDeadline()))
                                .append("** (").append(Formatter.formatTimestamp(e.getDeadline())).append(")\n");
                    } else {
                        builder.append(CommandReaction.ERROR.getUnicode()).append(" **")
                                .append(e.getMember().getEffectiveName())
                                .append("** should have contributed **")
                                .append(Formatter.formatLargestUnitBetween(e.getDeadline(), now))
                                .append(" ago** (").append(Formatter.formatTimestamp(e.getDeadline())).append(")\n");
                    }
                }
            }
        }

        List<MemberReportDetails> completelyInactive = reportDetails.stream()
                .filter(member -> member.isAnyInactivity() && !member.isAnyActivity())
                .sorted(Comparator.comparing(e -> e.getMember().getTimeJoined()))
                .collect(Collectors.toList());
        if (!completelyInactive.isEmpty()) {
            builder.append("\n__**Members that are completely inactive**__ - ")
                    .append(completelyInactive.size()).append("\n");
            for (MemberReportDetails e : completelyInactive) {
                builder.append(CommandReaction.ERROR.getUnicode()).append(" **")
                        .append(e.getMember().getEffectiveName())
                        .append("** has no recent activity data\n");
            }
        }

        if (builder.length() == 0) {
            event.reply("Nothing to report.", event::complete);
        } else {
            event.reply(builder.toString(), event::complete);
        }
    }

    @Data
    private static class MemberReportDetails {
        Member member;
        boolean initiate;
        Long joined;
        boolean contribution;
        Long lastOnline;
        Long lastMessage;
        final Map<Long, Long> lastPlayed = new HashMap<>();
        boolean anyActivity;
        boolean anyInactivity;
        OffsetDateTime deadline;
    }

    public void doMemberReport(CommandEvent event, Member member) {
        StringBuilder builder = new StringBuilder();
        OffsetDateTime now = OffsetDateTime.now();

        // role hierarchy
        boolean isInitiate = hasInitiateRole(event, member);
        boolean isMember = hasMemberRole(event, member);
        builder.append("Activity report for ").append(member.getAsMention()).append("\n\n__**Role**__\n");
        if (hasAdminRole(event, member)) {
            builder.append("Admin");
        } else if (hasModRole(event, member)) {
            builder.append("Mod");
        } else if (isMember) {
            builder.append("Member");
        } else if (isInitiate) {
            builder.append("Initiate");
        } else {
            builder.append("unknown");
        }

        builder.append("\n\n__**Activity**__\n");

        // joined
        Long joined = event.getTributeService().getStart(member);
        if (event.getTributeService().getRole(event.getGuild()) != null) {
            if (joined == null || joined < 1) {
                builder.append(isInitiate ? CommandReaction.ERROR.getUnicode() : CommandReaction.WARNING.getUnicode())
                        .append(" Joined: **unknown**\n");
            } else {
                builder.append(CommandReaction.OK.getUnicode()).append(" Joined: **")
                        .append(Formatter.formatLargestUnitAgo(joined)).append("** (")
                        .append(Formatter.formatTimestamp(joined)).append(")\n");
            }
        }

        // contribution
        Long contributionTimeoutInDays = event.getTributeService().getTimeout(event.getGuild());
        if (contributionTimeoutInDays != null) {
            boolean contribution = event.getTributeService().getTribute(member);
            long contributionDelayInDays = ObjectUtils.defaultIfNull(
                    event.getTributeService().getDelay(member), 0L);
            OffsetDateTime deadline = joined == null ? OffsetDateTime.MAX
                    : OffsetDateTime.ofInstant(Instant.ofEpochMilli(joined), ZoneOffset.UTC)
                    .plusDays(contributionTimeoutInDays + contributionDelayInDays);
            if (contribution) {
                builder.append(CommandReaction.OK.getUnicode());
            } else if (!isInitiate || deadline.isAfter(now)) {
                builder.append(CommandReaction.WARNING.getUnicode());
            } else {
                builder.append(CommandReaction.ERROR.getUnicode());
            }
            builder.append(" Contribution: **").append(contribution ? "yes" : "no").append("**");
            if (isInitiate) {
                builder.append(" (").append(Formatter.formatLargestUnitBetween(now, deadline))
                        .append(deadline.isAfter(now) ? " left" : " overdue").append(')');
            }
            builder.append('\n');
        }

        // last online
        Integer onlineTimeoutInDays = event.getInactivityService().getOnlineThreshold(event.getGuild());
        if (onlineTimeoutInDays != null) {
            Long lastOnline = event.getActivityService().getActivityOnline(member);
            if (lastOnline == null) {
                if (isMember) {
                    builder.append(CommandReaction.ERROR.getUnicode());
                } else {
                    builder.append(CommandReaction.WARNING.getUnicode());
                }
                builder.append(" Last online: **unknown**\n");
            } else {
                OffsetDateTime deadline = OffsetDateTime.ofInstant(Instant.ofEpochMilli(lastOnline), ZoneOffset.UTC)
                        .plusDays(onlineTimeoutInDays);
                if (deadline.isAfter(now)) {
                    builder.append(CommandReaction.OK.getUnicode());
                } else if (isMember) {
                    builder.append(CommandReaction.ERROR.getUnicode());
                } else {
                    builder.append(CommandReaction.WARNING.getUnicode());
                }
                builder.append(" Last online: **").append(Formatter.formatLargestUnitAgo(lastOnline)).append("** (")
                        .append(Formatter.formatTimestamp(lastOnline)).append(")\n");
            }
        }

        // last message
        Integer messageTimeoutInDays = event.getInactivityService().getMessageThreshold(event.getGuild());
        if (messageTimeoutInDays != null) {
            Long lastMessage = event.getActivityService().getActivityMessage(member);
            if (lastMessage == null) {
                if (isMember) {
                    builder.append(CommandReaction.ERROR.getUnicode());
                } else {
                    builder.append(CommandReaction.WARNING.getUnicode());
                }
                builder.append(" Last message: **unknown**\n");
            } else {
                OffsetDateTime deadline = OffsetDateTime.ofInstant(Instant.ofEpochMilli(lastMessage), ZoneOffset.UTC)
                        .plusDays(messageTimeoutInDays);
                if (deadline.isAfter(now)) {
                    builder.append(CommandReaction.OK.getUnicode());
                } else if (isMember) {
                    builder.append(CommandReaction.ERROR.getUnicode());
                } else {
                    builder.append(CommandReaction.WARNING.getUnicode());
                }
                builder.append(" Last message: **").append(Formatter.formatLargestUnitAgo(lastMessage)).append("** (")
                        .append(Formatter.formatTimestamp(lastMessage)).append(")\n");
            }
        }

        // last played
        event.getInactivityService().getPlayingThresholds(event.getGuild()).forEach((gameId, playingTimeoutInDays) -> {
            String gameName = event.getGameService().getGameName(gameId);
            Long lastPlayed = event.getActivityService().getActivityPlaying(member, gameId);
            if (lastPlayed == null) {
                if (isMember) {
                    builder.append(CommandReaction.ERROR.getUnicode());
                } else {
                    builder.append(CommandReaction.WARNING.getUnicode());
                }
                builder.append(" Last played **").append(gameName).append("** (").append(gameId)
                        .append("): **unknown**\n");
            } else {
                OffsetDateTime deadline = OffsetDateTime.ofInstant(Instant.ofEpochMilli(lastPlayed), ZoneOffset.UTC)
                        .plusDays(playingTimeoutInDays);
                if (deadline.isAfter(now)) {
                    builder.append(CommandReaction.OK.getUnicode());
                } else if (isMember) {
                    builder.append(CommandReaction.ERROR.getUnicode());
                } else {
                    builder.append(CommandReaction.WARNING.getUnicode());
                }
                builder.append(" Last played: **").append(gameName).append("** (").append(gameId).append("): **")
                        .append(Formatter.formatLargestUnitAgo(lastPlayed)).append("** (")
                        .append(Formatter.formatTimestamp(lastPlayed)).append(")\n");
            }
        });

        event.reply(builder.toString(), event::complete);
    }

    private boolean hasInitiateRole(CommandEvent event, Member member) {
        Role role = event.getTributeService().getRole(event.getGuild());
        return role != null && member.getRoles().contains(role);
    }

    private boolean hasMemberRole(CommandEvent event, Member member) {
        List<Role> roles = event.getPermissionService()
                .getRolePermissions(event.getGuild(), Node.GEOBOT_USER)
                .stream().filter(e -> e.getFlag() > 0).map(e -> event.getGuild().getRoleById(e.getId()))
                .filter(Objects::nonNull).collect(Collectors.toList());
        return member.getRoles().stream().anyMatch(roles::contains);
    }

    private boolean hasModRole(CommandEvent event, Member member) {
        List<Role> roles = event.getPermissionService()
                .getRolePermissions(event.getGuild(), Node.GEOBOT_MOD)
                .stream().filter(e -> e.getFlag() > 0).map(e -> event.getGuild().getRoleById(e.getId()))
                .filter(Objects::nonNull).collect(Collectors.toList());
        return member.getRoles().stream().anyMatch(roles::contains);
    }

    private boolean hasAdminRole(CommandEvent event, Member member) {
        List<Role> roles = event.getPermissionService()
                .getRolePermissions(event.getGuild(), Node.GEOBOT_ADMIN)
                .stream().filter(e -> e.getFlag() > 0).map(e -> event.getGuild().getRoleById(e.getId()))
                .filter(Objects::nonNull).collect(Collectors.toList());
        return member.getRoles().stream().anyMatch(roles::contains);
    }

}
