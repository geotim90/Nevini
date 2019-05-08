package de.nevini.modules.core.permission;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;
import de.nevini.resolvers.Resolvers;
import lombok.Data;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class PermissionTarget {

    private static final Pattern ALL_FLAG = Pattern.compile("(?i)(?:(?:--|//)all|[-/]a)");
    private static final Pattern SERVER_FLAG = Pattern.compile("(?i)(?:(?:--|//)(?:server|guild)|[-/][sg])");

    @NonNull
    private final CommandEvent event;
    @NonNull
    private final Consumer<PermissionTarget> callback;

    private boolean all;
    private boolean server;
    private Role role;
    private Member member;
    private TextChannel channel;
    private Collection<Node> nodes;

    public void get() {
        all = event.getOptions().getOptions().stream().map(ALL_FLAG::matcher).anyMatch(Matcher::matches);
        server = event.getOptions().getOptions().stream().map(SERVER_FLAG::matcher).anyMatch(Matcher::matches);
        Resolvers.ROLE.resolveOptionOrInputIfExists(event, this::acceptRole);
    }

    private void acceptRole(Role role) {
        this.role = role;
        Resolvers.MEMBER.resolveOptionOrDefaultIfExists(event, event.getMember(), this::acceptMember);
    }

    private void acceptMember(Member member) {
        this.member = member;
        if (role != null && member != null) {
            event.reply(CommandReaction.WARNING, "You cannot select a role and a user at the same time!");
        } else {
            Resolvers.CHANNEL.resolveOptionOrDefaultIfExists(event, event.getTextChannel(), this::acceptChannel);
        }
    }

    private void acceptChannel(TextChannel channel) {
        this.channel = channel;
        if (server && channel != null) {
            event.reply(CommandReaction.WARNING, "You cannot select the server and a channel at the same time!");
        } else {
            Resolvers.NODE.resolveOptionListOrInputIfExists(event, this::acceptNodes);
        }
    }

    private void acceptNodes(List<Node> nodes) {
        this.nodes = nodes;
        if (all && nodes != null) {
            event.reply(CommandReaction.WARNING, "You cannot select all nodes and specific nodes at the same time!");
        } else {
            callback.accept(this);
        }
    }

}
