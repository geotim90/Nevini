package de.nevini.modules.core.permission;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;
import de.nevini.resolvers.Resolvers;
import lombok.Data;
import lombok.NonNull;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class PermissionOptions {

    private static final Pattern ALL_FLAG = Pattern.compile("(?i)(?:(?:--|//)all|[-/]a)");
    private static final Pattern SERVER_FLAG = Pattern.compile("(?i)(?:(?:--|//)(?:server|guild)|[-/][sg])");

    @NonNull
    private final CommandEvent event;
    private final boolean nodeRequired;
    private final boolean multipleNodes;
    @NonNull
    private final Consumer<PermissionOptions> callback;

    private boolean server;
    private Permission permission;
    private Role role;
    private Member member;
    private TextChannel channel;
    private Node node;
    private List<Node> nodes;

    public void get() {
        server = event.getOptions().getOptions().stream().map(SERVER_FLAG::matcher).anyMatch(Matcher::matches);
        Resolvers.PERMISSION.resolveOptionOrInputIfExists(event, this::acceptPermission);
    }

    private void acceptPermission(Permission permission) {
        this.permission = permission;
        Resolvers.ROLE.resolveOptionOrInputIfExists(event, this::acceptRole);
    }

    private void acceptRole(Role role) {
        this.role = role;
        if (permission != null && role != null) {
            event.reply(CommandReaction.WARNING, "You cannot select a permission and a role at the same time!");
        } else {
            Resolvers.MEMBER.resolveOptionOrDefaultIfExists(event, event.getMember(), this::acceptMember);
        }
    }

    private void acceptMember(Member member) {
        this.member = member;
        if (permission != null && member != null) {
            event.reply(CommandReaction.WARNING, "You cannot select a permission and a user at the same time!");
        } else if (role != null && member != null) {
            event.reply(CommandReaction.WARNING, "You cannot select a role and a user at the same time!");
        } else {
            Resolvers.CHANNEL.resolveOptionOrDefaultIfExists(event, event.getTextChannel(), this::acceptChannel);
        }
    }

    private void acceptChannel(TextChannel channel) {
        this.channel = channel;
        if (server && channel != null) {
            event.reply(CommandReaction.WARNING, "You cannot select the server and a channel at the same time!");
        } else if (multipleNodes) {
            if (nodeRequired) {
                Resolvers.NODE.resolveListArgumentOrOptionOrInput(event, this::acceptNodes);
            } else {
                Resolvers.NODE.resolveListArgumentOrOptionOrInputIfExists(event, this::acceptNodes);
            }
        } else {
            if (nodeRequired) {
                Resolvers.NODE.resolveArgumentOrOptionOrInput(event, this::acceptNode);
            } else {
                Resolvers.NODE.resolveArgumentOrOptionOrInputIfExists(event, this::acceptNode);
            }
        }
    }

    private void acceptNodes(List<Node> nodes) {
        if (event.getOptions().getOptions().stream().map(ALL_FLAG::matcher).anyMatch(Matcher::matches)) {
            if (nodes == null) {
                this.nodes = Arrays.asList(Node.values());
                callback.accept(this);
            } else {
                event.reply(CommandReaction.WARNING, "You cannot select all nodes and specific nodes at the same time!");
            }
        } else {
            this.nodes = ObjectUtils.defaultIfNull(nodes, Collections.emptyList());
            callback.accept(this);
        }
    }

    private void acceptNode(Node node) {
        this.node = node;
        callback.accept(this);
    }

}