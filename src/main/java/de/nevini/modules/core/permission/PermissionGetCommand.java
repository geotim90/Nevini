package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class PermissionGetCommand extends Command {

    public PermissionGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.CORE_PERMISSION_GET)
                .description("displays effective permissions for commands")
                .syntax("[<options>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        new PermissionOptions(event, target -> acceptTarget(event, target)).get();
    }

    private void acceptTarget(CommandEvent event, PermissionOptions permissionTarget) {
        if (permissionTarget.getNodes().isEmpty()) {
            if (permissionTarget.isAll()) {
                permissionTarget.setNodes(Arrays.asList(Node.values()));
            } else {
                permissionTarget.setNodes(Arrays.stream(Node.values())
                        .filter(node -> event.getModuleService().isModuleActive(event.getGuild(), node.getModule()))
                        .collect(Collectors.toList()));
            }
        }
        if (permissionTarget.isServer()) {
            if (permissionTarget.getRole() != null) {
                displayRolePermissions(event, permissionTarget.getRole(), permissionTarget.getNodes());
            } else {
                Member user = ObjectUtils.defaultIfNull(permissionTarget.getMember(), event.getMember());
                displayUserPermissions(event, user, permissionTarget.getNodes());
            }
        } else {
            TextChannel channel = ObjectUtils.defaultIfNull(permissionTarget.getChannel(), event.getTextChannel());
            if (permissionTarget.getRole() != null) {
                displayChannelRolePermissions(event, channel, permissionTarget.getRole(), permissionTarget.getNodes());
            } else {
                Member user = ObjectUtils.defaultIfNull(permissionTarget.getMember(), event.getMember());
                displayChannelUserPermissions(event, channel, user, permissionTarget.getNodes());
            }
        }
    }

    private void displayRolePermissions(CommandEvent event, Role role, Collection<Node> nodes) {
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective server permissions for **" + role.getName() + "**:");
        for (Node node : nodes) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                Optional<Boolean> override = event.getPermissionService().getRolePermission(role, node);
                String value = resolvePermission(node, override.orElse(null), role.getPermissions());
                embedBuilder.addField(node.getNode(), value, true);
            }
        }
        event.reply(embedBuilder);
    }

    private void displayUserPermissions(CommandEvent event, Member user, Collection<Node> nodes) {
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective server permissions for **" + user.getEffectiveName() + "**:");
        for (Node node : nodes) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                Optional<Boolean> override = event.getPermissionService().getUserPermission(user, node);
                String value = resolvePermission(node, override.orElse(null), user.getPermissions());
                embedBuilder.addField(node.getNode(), value, true);
            }
        }
        event.reply(embedBuilder);
    }

    private void displayChannelRolePermissions(CommandEvent event, TextChannel channel, Role role, Collection<Node> nodes) {
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective permissions for **" + role.getName() + "** in **" + channel.getName() + "**:");
        for (Node node : nodes) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                Optional<Boolean> override = event.getPermissionService().getChannelRolePermission(channel, role, node);
                String value = resolvePermission(node, override.orElse(null), role.getPermissions());
                embedBuilder.addField(node.getNode(), value, true);
            }
        }
        event.reply(embedBuilder);
    }

    private void displayChannelUserPermissions(CommandEvent event, TextChannel channel, Member user, Collection<Node> nodes) {
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective permissions for **" + user.getEffectiveName() + "** in **" + channel.getName() + "**:");
        for (Node node : nodes) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                Optional<Boolean> override = event.getPermissionService().getChannelUserPermission(channel, user, node);
                String value = resolvePermission(node, override.orElse(null), user.getPermissions());
                embedBuilder.addField(node.getNode(), value, true);
            }
        }
        event.reply(embedBuilder);
    }

    private String resolvePermission(Node node, Boolean override, Collection<Permission> permissions) {
        if (override == null) {
            long required = Permission.getRaw(node.getDefaultPermissions());
            long available = Permission.getRaw(permissions);
            long missing = required & ~available;
            if (missing == 0) {
                return CommandReaction.DEFAULT_OK.getUnicode();
            } else {
                return CommandReaction.DEFAULT_NOK.getUnicode();
            }
        } else if (override) {
            return CommandReaction.OK.getUnicode();
        } else {
            return CommandReaction.PROHIBITED.getUnicode();
        }
    }

}
