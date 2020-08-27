package de.nevini.modules.core.permission.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.modules.core.permission.data.PermissionData;
import de.nevini.modules.core.permission.services.PermissionService;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.stream.Collectors;

class PermissionOverridesCommand extends Command {

    PermissionOverridesCommand() {
        super(CommandDescriptor.builder()
                .keyword("overrides")
                .aliases(new String[]{"override"})
                .node(Node.CORE_PERMISSION_OVERRIDES)
                .description("displays all configured overrides for the specified node or nodes")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.NODE.describe(true, true),
                        PermissionOptions.ALL_FLAG.describe(),
                })
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
        String message = event.getPermissionService().getPermissions(event.getGuild(), options.getNodes()).stream()
                .sorted(Comparator.comparing(PermissionData::getNode).thenComparing(PermissionData::getType))
                .map(data -> render(event, data) + '\n')
                .collect(Collectors.joining());
        if (message.length() == 0) {
            event.reply("No matching overrides found.", event::complete);
        } else {
            event.reply(message, event::complete);
        }
    }

    private String render(CommandEvent event, PermissionData data) {
        String prefix;
        if (data.getFlag() == 0) {
            return "";
        } else if (data.getFlag() > 0) {
            prefix = CommandReaction.OK.getUnicode();
        } else {
            prefix = CommandReaction.PROHIBITED.getUnicode();
        }
        prefix += " - **" + data.getNode() + "** for ";
        TextChannel channel = event.getGuild().getTextChannelById(data.getChannel());
        String channelName = channel == null ? data.getChannel().toString() : channel.getName();
        switch (data.getType()) {
            case PermissionService.SERVER:
                return prefix + "*everyone* on **" + event.getGuild().getName() + "**";
            case PermissionService.PERMISSION:
                EnumSet<Permission> permissions = Permission.getPermissions(data.getId());
                String permissionName = permissions.stream().findFirst().map(Permission::getName)
                        .orElse(data.getId().toString());
                return prefix + "**" + permissionName + "** on **" + event.getGuild().getName() + "**";
            case PermissionService.ROLE:
                Role role = event.getGuild().getRoleById(data.getId());
                String roleName = role == null ? data.getId().toString() : role.getName();
                return prefix + "**" + roleName + "** on **" + event.getGuild().getName() + "**";
            case PermissionService.USER:
                Member member = event.getGuild().getMemberById(data.getId());
                String memberName = member == null ? data.getId().toString() : member.getEffectiveName();
                return prefix + "**" + memberName + "** on **" + event.getGuild().getName() + "**";
            case PermissionService.CHANNEL:
                return prefix + "*everyone* in **" + channelName + "**";
            case PermissionService.CHANNEL_PERMISSION:
                EnumSet<Permission> channelPermissions = Permission.getPermissions(data.getId());
                String channelPermissionName = channelPermissions.stream().findFirst().map(Permission::getName)
                        .orElse(data.getId().toString());
                return prefix + "**" + channelPermissionName + "** in **" + channelName + "**";
            case PermissionService.CHANNEL_ROLE:
                Role channelRole = event.getGuild().getRoleById(data.getId());
                String channelRoleName = channelRole == null ? data.getId().toString() : channelRole.getName();
                return prefix + "**" + channelRoleName + "** in **" + channelName + "**";
            case PermissionService.CHANNEL_USER:
                Member channelMember = event.getGuild().getMemberById(data.getId());
                String channelMemberName = channelMember == null ? data.getId().toString()
                        : channelMember.getEffectiveName();
                return prefix + "**" + channelMemberName + "** in **" + channelName + "**";
            default:
                return "";
        }
    }

}
