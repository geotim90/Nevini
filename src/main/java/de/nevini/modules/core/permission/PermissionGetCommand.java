package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

class PermissionGetCommand extends Command {

    PermissionGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.CORE_PERMISSION_GET)
                .description("displays effective permissions for bot commands")
                .options(PermissionOptions.getCommandOptionDescriptors(true))
                .details(PermissionOptions.getCommandDescriptorDetails())
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        new PermissionOptions(event, false, true, options -> acceptOptions(event, options)).get();
    }

    private void acceptOptions(CommandEvent event, PermissionOptions options) {
        if (options.getNodes().isEmpty()) {
            options.setNodes(Arrays.stream(Node.values())
                    .filter(node -> node.getNode().startsWith(node.getModule().getName()))
                    .filter(node -> event.getModuleService().isModuleActive(event.getGuild(), node.getModule()))
                    .collect(Collectors.toList()));
        }
        if (options.isGuild()) {
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
            if (options.getPermission() != null) {
                displayChannelPermissionPermissions(event, options);
            } else if (options.getRole() != null) {
                displayChannelRolePermissions(event, options);
            } else if (options.getMember() != null) {
                displayChannelUserPermissions(event, options);
            } else if (options.getChannel() != null) {
                displayChannelPermissions(event, options);
            } else {
                displayChannelUserPermissions(event, options);
            }
        }
    }

    private void displayServerPermissions(CommandEvent event, PermissionOptions options) {
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective server permissions:");
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                boolean allowed = event.getPermissionService().hasServerPermission(event.getGuild(), node);
                embedBuilder.addField(node.getNode(),
                        allowed ? CommandReaction.OK.getUnicode() : CommandReaction.PROHIBITED.getUnicode(),
                        true);
            }
        }
        event.reply(embedBuilder, event::complete);
    }

    private void displayPermissionPermissions(CommandEvent event, PermissionOptions options) {
        Permission permission = options.getPermission();
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective server permissions for **" + permission.getName() + "**:");
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                boolean allowed = event.getPermissionService()
                        .hasPermissionPermission(event.getGuild(), permission, node);
                embedBuilder.addField(node.getNode(),
                        allowed ? CommandReaction.OK.getUnicode() : CommandReaction.PROHIBITED.getUnicode(),
                        true);
            }
        }
        event.reply(embedBuilder, event::complete);
    }

    private void displayRolePermissions(CommandEvent event, PermissionOptions options) {
        Role role = options.getRole();
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective server permissions for **" + role.getName() + "**:");
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                boolean allowed = event.getPermissionService().hasRolePermission(role, node);
                embedBuilder.addField(node.getNode(),
                        allowed ? CommandReaction.OK.getUnicode() : CommandReaction.PROHIBITED.getUnicode(),
                        true);
            }
        }
        event.reply(embedBuilder, event::complete);
    }

    private void displayUserPermissions(CommandEvent event, PermissionOptions options) {
        Member member = options.getMember();
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective server permissions for **" + member.getEffectiveName() + "**:");
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                boolean allowed = event.getPermissionService().hasUserPermission(member, node);
                embedBuilder.addField(node.getNode(),
                        allowed ? CommandReaction.OK.getUnicode() : CommandReaction.PROHIBITED.getUnicode(),
                        true);
            }
        }
        event.reply(embedBuilder, event::complete);
    }

    private void displayChannelPermissions(CommandEvent event, PermissionOptions options) {
        TextChannel channel = options.getChannel();
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription("Effective permissions for **" + channel.getName() + "**:");
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                boolean allowed = event.getPermissionService().hasChannelPermission(channel, node);
                embedBuilder.addField(node.getNode(),
                        allowed ? CommandReaction.OK.getUnicode() : CommandReaction.PROHIBITED.getUnicode(),
                        true);
            }
        }
        event.reply(embedBuilder, event::complete);
    }

    private void displayChannelPermissionPermissions(CommandEvent event, PermissionOptions options) {
        Permission permission = options.getPermission();
        TextChannel channel = ObjectUtils.defaultIfNull(options.getChannel(), event.getTextChannel());
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription(
                "Effective permissions for **" + permission.getName() + "** in **" + channel.getName() + "**:");
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                boolean allowed = event.getPermissionService()
                        .hasChannelPermissionPermission(channel, permission, node);
                embedBuilder.addField(node.getNode(),
                        allowed ? CommandReaction.OK.getUnicode() : CommandReaction.PROHIBITED.getUnicode(),
                        true);
            }
        }
        event.reply(embedBuilder, event::complete);
    }

    private void displayChannelRolePermissions(CommandEvent event, PermissionOptions options) {
        Role role = options.getRole();
        TextChannel channel = ObjectUtils.defaultIfNull(options.getChannel(), event.getTextChannel());
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription(
                "Effective permissions for **" + role.getName() + "** in **" + channel.getName() + "**:"
        );
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                boolean allowed = event.getPermissionService().hasChannelRolePermission(channel, role, node);
                embedBuilder.addField(node.getNode(),
                        allowed ? CommandReaction.OK.getUnicode() : CommandReaction.PROHIBITED.getUnicode(),
                        true);
            }
        }
        event.reply(embedBuilder, event::complete);
    }

    private void displayChannelUserPermissions(CommandEvent event, PermissionOptions options) {
        Member member = ObjectUtils.defaultIfNull(options.getMember(), event.getMember());
        TextChannel channel = ObjectUtils.defaultIfNull(options.getChannel(), event.getTextChannel());
        EmbedBuilder embedBuilder = event.createEmbedBuilder();
        embedBuilder.setDescription(
                "Effective permissions for **" + member.getEffectiveName() + "** in **" + channel.getName() + "**:"
        );
        for (Node node : options.getNodes()) {
            if (!event.getModuleService().isModuleActive(event.getGuild(), node.getModule())) {
                embedBuilder.addField(node.getNode(), CommandReaction.DISABLED.getUnicode(), true);
            } else {
                boolean allowed = event.getPermissionService().hasChannelUserPermission(channel, member, node);
                embedBuilder.addField(node.getNode(),
                        allowed ? CommandReaction.OK.getUnicode() : CommandReaction.PROHIBITED.getUnicode(),
                        true);
            }
        }
        event.reply(embedBuilder, event::complete);
    }

}
