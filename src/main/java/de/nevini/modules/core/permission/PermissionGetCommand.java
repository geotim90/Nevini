package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.*;
import java.util.stream.Collectors;

public class PermissionGetCommand extends Command {

    public PermissionGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.CORE_PERMISSION_GET)
                .description("displays effective permissions for bot commands")
                .syntax("[<options>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        new PermissionOptions(event, false, true, options -> acceptOptions(event, options)).get();
    }

    private void acceptOptions(CommandEvent event, PermissionOptions options) {
        if (options.getNodes().isEmpty()) {
            options.setNodes(Arrays.stream(Node.values())
                    .filter(node -> event.getModuleService().isModuleActive(event.getGuild(), node.getModule()))
                    .collect(Collectors.toList()));
        }
        if (options.isServer()) {
            if (options.getPermission() != null) {
                displayPermissionPermissions(event, options);
            } else if (options.getRole() != null) {
                displayRolePermissions(event, options);
            } else if (options.getMember() != null) {
                displayUserPermissions(event, options);
            } else {
                displayServerPermissions(event, options);
            }
        } else {
            if (options.getChannel() == null) {
                options.setChannel(event.getTextChannel());
            }
            if (options.getPermission() != null) {
                displayChannelPermissionPermissions(event, options);
            } else if (options.getRole() != null) {
                displayChannelRolePermissions(event, options);
            } else if (options.getMember() != null) {
                displayChannelUserPermissions(event, options);
            } else {
                displayChannelPermissions(event, options);
            }
        }
    }

    private void displayServerPermissions(CommandEvent event, PermissionOptions options) {
        Guild server = event.getGuild();
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective server permissions:");
        List<Permission> permissions = server.getPublicRole().getPermissions();
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                Optional<Boolean> override = event.getPermissionService().hasServerPermission(event.getGuild(), node);
                String value = resolvePermission(node, override.orElse(null), permissions);
                embedBuilder.addField(node.getNode(), value, true);
            }
        }
        event.reply(embedBuilder);
    }

    private void displayPermissionPermissions(CommandEvent event, PermissionOptions options) {
        Permission permission = options.getPermission();
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective server permissions for **" + permission.getName() + "**:");
        List<Permission> permissions = Collections.singletonList(permission);
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                Optional<Boolean> override = event.getPermissionService().hasPermissionPermission(event.getGuild(), permission, node);
                String value = resolvePermission(node, override.orElse(null), permissions);
                embedBuilder.addField(node.getNode(), value, true);
            }
        }
        event.reply(embedBuilder);
    }

    private void displayRolePermissions(CommandEvent event, PermissionOptions options) {
        Role role = options.getRole();
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective server permissions for **" + role.getName() + "**:");
        List<Permission> permissions = role.getPermissions();
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                Optional<Boolean> override = event.getPermissionService().hasRolePermission(role, node);
                String value = resolvePermission(node, override.orElse(null), permissions);
                embedBuilder.addField(node.getNode(), value, true);
            }
        }
        event.reply(embedBuilder);
    }

    private void displayUserPermissions(CommandEvent event, PermissionOptions options) {
        Member member = options.getMember();
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective server permissions for **" + member.getEffectiveName() + "**:");
        List<Permission> permissions = member.getPermissions();
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                Optional<Boolean> override = event.getPermissionService().hasUserPermission(member, node);
                String value = resolvePermission(node, override.orElse(null), permissions);
                embedBuilder.addField(node.getNode(), value, true);
            }
        }
        event.reply(embedBuilder);
    }

    private void displayChannelPermissions(CommandEvent event, PermissionOptions options) {
        TextChannel channel = options.getChannel();
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective permissions for **" + channel.getName() + "**:");
        List<Permission> permissions = Permission.getPermissions(PermissionUtil.getEffectivePermission(channel, event.getGuild().getPublicRole()));
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                Optional<Boolean> override = event.getPermissionService().hasChannelPermission(channel, node);
                String value = resolvePermission(node, override.orElse(null), permissions);
                embedBuilder.addField(node.getNode(), value, true);
            }
        }
        event.reply(embedBuilder);
    }

    private void displayChannelPermissionPermissions(CommandEvent event, PermissionOptions options) {
        Permission permission = options.getPermission();
        TextChannel channel = options.getChannel();
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective permissions for **" + permission.getName() + "** in **" + channel.getName() + "**:");
        List<Permission> permissions = Collections.singletonList(permission);
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                Optional<Boolean> override = event.getPermissionService().hasChannelPermissionPermission(channel, permission, node);
                String value = resolvePermission(node, override.orElse(null), permissions);
                embedBuilder.addField(node.getNode(), value, true);
            }
        }
        event.reply(embedBuilder);
    }

    private void displayChannelRolePermissions(CommandEvent event, PermissionOptions options) {
        Role role = options.getRole();
        TextChannel channel = options.getChannel();
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective permissions for **" + role.getName() + "** in **" + channel.getName() + "**:");
        List<Permission> permissions = Permission.getPermissions(PermissionUtil.getEffectivePermission(channel, role));
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                Optional<Boolean> override = event.getPermissionService().hasChannelRolePermission(channel, role, node);
                String value = resolvePermission(node, override.orElse(null), permissions);
                embedBuilder.addField(node.getNode(), value, true);
            }
        }
        event.reply(embedBuilder);
    }

    private void displayChannelUserPermissions(CommandEvent event, PermissionOptions options) {
        Member member = options.getMember();
        TextChannel channel = options.getChannel();
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective permissions for **" + member.getEffectiveName() + "** in **" + channel.getName() + "**:");
        List<Permission> permissions = member.getPermissions(channel);
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                Optional<Boolean> override = event.getPermissionService().hasChannelUserPermission(channel, member, node);
                String value = resolvePermission(node, override.orElse(null), permissions);
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
            if (missing == 0 || permissions.contains(Permission.ADMINISTRATOR)) {
                return CommandReaction.DEFAULT_OK.getUnicode();
            } else {
                return CommandReaction.DEFAULT_NOK.getUnicode();
            }
        } else if (override) {
            return CommandReaction.OK.getUnicode();
        } else if (permissions.contains(Permission.ADMINISTRATOR)) {
            return CommandReaction.DEFAULT_OK.getUnicode();
        } else {
            return CommandReaction.PROHIBITED.getUnicode();
        }
    }

}
