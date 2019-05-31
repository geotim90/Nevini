package de.nevini.modules.core.permission;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.command.CommandReaction;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
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

    static CommandOptionDescriptor[] getCommandOptionDescriptors(boolean resolveNodeList) {
        return new CommandOptionDescriptor[]{
                Resolvers.NODE.describe(true, resolveNodeList),
                CommandOptionDescriptor.builder()
                        .syntax("--all")
                        .description("Explicitly refers to all permission nodes.")
                        .keyword("--all")
                        .aliases(new String[]{"//all", "-a", "/a"})
                        .build(),
                CommandOptionDescriptor.builder()
                        .syntax("--server")
                        .description("Changes the scope to server-wide permissions instead of channel-specific permissions.")
                        .keyword("--server")
                        .aliases(new String[]{"//server", "--guild", "//guild", "-s", "/s", "-g", "/g"})
                        .build(),
                Resolvers.PERMISSION.describe(),
                Resolvers.ROLE.describe(),
                Resolvers.MEMBER.describe(),
                Resolvers.CHANNEL.describe()
        };
    }

    static String getCommandDescriptorDetails() {
        return "Permission node overrides for bot commands are applied in the following order:\n"
                + "1. Default permissions\n"
                + "2. Server permissions\n"
                + "3. Permissions based on effective permissions (e.g. \"Manage Server\")\n"
                + "4. Role permissions\n"
                + "5. User permissions\n"
                + "6. Channel permissions\n"
                + "7. Channel-specific permissions based on effective permissions (e.g. \"Manage Server\")\n"
                + "8. Channel-specific role permissions\n"
                + "9. Channel-specific user permissions\n\n"
                + "If multiple overrides on the same \"level\" disagree with each other (e.g. conflicting roles), **allow** will trump **deny**.\n"
                + "Server owners and administrators are not restricted by permission node overrides.\n"
                + "Users can only configure permissions for permission nodes they have themselves.\n"
                + "Users can only configure permissions for roles of a lower position than their highest role.\n"
                + "Users can only configure permissions for users whose highest role is lower than their highest role.";
    }

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
