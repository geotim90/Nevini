package de.nevini.modules.core.permission.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.lang3.ObjectUtils;

class PermissionDebugCommand extends Command {

    PermissionDebugCommand() {
        super(CommandDescriptor.builder()
                .keyword("debug")
                .node(Node.CORE_PERMISSION_DEBUG)
                .description("displays a permission node trace for bot commands")
                .options(PermissionOptions.getCommandOptionDescriptors(false))
                .details(PermissionOptions.getCommandDescriptorDetails())
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        new PermissionOptions(event, true, false, options -> acceptOptions(event, options)).get();
    }

    private void acceptOptions(CommandEvent event, PermissionOptions options) {
        StringBuilder builder = new StringBuilder(
                "Permission to execute commands with permission node **" + options.getNode().getNode() + "**\n"
        );
        if (options.isGuild()) {
            if (options.getPermission() != null) {
                appendPermissionTrace(event, options, builder);
            } else if (options.getRole() != null) {
                appendRoleTrace(event, options, builder);
            } else if (options.getMember() != null) {
                appendUserTrace(event, options, builder);
            } else {
                appendServerTrace(event, options, builder);
            }
        } else {
            if (options.getPermission() != null) {
                appendChannelPermissionTrace(event, options, builder);
            } else if (options.getRole() != null) {
                appendChannelRoleTrace(event, options, builder);
            } else if (options.getMember() != null) {
                appendChannelUserTrace(event, options, builder);
            } else if (options.getChannel() != null) {
                appendChannelTrace(event, options, builder);
            } else {
                appendChannelUserTrace(event, options, builder);
            }
        }
        event.reply(builder.toString(), event::complete);
    }

    private void appendChannelUserTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        TextChannel channel = ObjectUtils.defaultIfNull(options.getChannel(), event.getTextChannel());
        Member user = ObjectUtils.defaultIfNull(options.getMember(), event.getMember());
        Boolean channelUserOverride = event.getPermissionService()
                .getChannelUserPermission(channel, user, options.getNode()).orElse(null);
        boolean channelUserPermission = event.getPermissionService()
                .hasChannelUserPermission(channel, user, options.getNode());
        append(builder, channelUserOverride, channelUserPermission,
                "__**" + user.getEffectiveName() + "** in **" + channel.getName() + "**__");
        Boolean channelRoleOverride = event.getPermissionService()
                .getChannelRolePermission(channel, user, options.getNode()).orElse(null);
        boolean channelRolePermission = event.getPermissionService()
                .hasChannelRolePermission(channel, user, options.getNode());
        append(builder, channelRoleOverride, channelRolePermission,
                "**" + user.getEffectiveName() + "** roles in **" + channel.getName() + "**");
        Boolean channelPermissionOverride = event.getPermissionService()
                .getChannelPermissionPermission(channel, user, options.getNode()).orElse(null);
        boolean channelPermissionPermission = event.getPermissionService()
                .hasChannelPermissionPermission(channel, user, options.getNode());
        append(builder, channelPermissionOverride, channelPermissionPermission,
                "**" + user.getEffectiveName() + "** permissions in **" + channel.getName() + "**");
        Boolean channelOverride = event.getPermissionService().getChannelPermission(channel, options.getNode())
                .orElse(null);
        boolean channelPermission = event.getPermissionService().hasChannelPermission(channel, options.getNode());
        append(builder, channelOverride, channelPermission, "*everyone* in **" + channel.getName() + "**");
        appendUserTrace(event, options, builder);
    }

    private void appendChannelRoleTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        TextChannel channel = ObjectUtils.defaultIfNull(options.getChannel(), event.getTextChannel());
        Boolean channelRoleOverride = event.getPermissionService()
                .getChannelRolePermission(channel, options.getRole(), options.getNode()).orElse(null);
        boolean channelRolePermission = event.getPermissionService()
                .hasChannelRolePermission(channel, options.getRole(), options.getNode());
        append(builder, channelRoleOverride, channelRolePermission,
                "**" + options.getRole().getName() + "** in **" + channel.getName() + "**");
        Boolean channelPermissionOverride = event.getPermissionService()
                .getChannelPermissionPermission(channel, options.getRole(), options.getNode()).orElse(null);
        boolean channelPermissionPermission = event.getPermissionService()
                .hasChannelPermissionPermission(channel, options.getRole(), options.getNode());
        append(builder, channelPermissionOverride, channelPermissionPermission,
                "**" + options.getRole().getName() + "** permissions in **" + channel.getName() + "**");
        Boolean channelOverride = event.getPermissionService().getChannelPermission(channel, options.getNode())
                .orElse(null);
        boolean channelPermission = event.getPermissionService().hasChannelPermission(channel, options.getNode());
        append(builder, channelOverride, channelPermission, "*everyone* in **" + channel.getName() + "**");
        appendRoleTrace(event, options, builder);
    }

    private void appendChannelPermissionTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        TextChannel channel = ObjectUtils.defaultIfNull(options.getChannel(), event.getTextChannel());
        Boolean channelPermissionOverride = event.getPermissionService()
                .getChannelPermissionPermission(channel, options.getPermission(), options.getNode()).orElse(null);
        boolean channelPermissionPermission = event.getPermissionService()
                .hasChannelPermissionPermission(channel, options.getPermission(), options.getNode());
        append(builder, channelPermissionOverride, channelPermissionPermission,
                "**" + options.getPermission().getName() + "** in **" + channel.getName() + "**");
        Boolean channelOverride = event.getPermissionService().getChannelPermission(channel, options.getNode())
                .orElse(null);
        boolean channelPermission = event.getPermissionService().hasChannelPermission(channel, options.getNode());
        append(builder, channelOverride, channelPermission, "*everyone* in **" + channel.getName() + "**");
        appendPermissionTrace(event, options, builder);
    }

    private void appendChannelTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        Boolean channelOverride = event.getPermissionService()
                .getChannelPermission(options.getChannel(), options.getNode()).orElse(null);
        boolean channelPermission = event.getPermissionService()
                .hasChannelPermission(options.getChannel(), options.getNode());
        append(builder, channelOverride, channelPermission, "*everyone* in **" + options.getChannel().getName() + "**");
        appendServerTrace(event, options, builder);
    }

    private void appendUserTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        Member user = ObjectUtils.defaultIfNull(options.getMember(), event.getMember());
        Boolean userOverride = event.getPermissionService().getUserPermission(user, options.getNode()).orElse(null);
        boolean userPermission = event.getPermissionService().hasUserPermission(user, options.getNode());
        append(builder, userOverride, userPermission,
                "**" + user.getEffectiveName() + "** on **" + event.getGuild().getName() + "**");
        Boolean roleOverride = event.getPermissionService().getRolePermission(user, options.getNode()).orElse(null);
        boolean rolePermission = event.getPermissionService().hasRolePermission(user, options.getNode());
        append(builder, roleOverride, rolePermission,
                "**" + user.getEffectiveName() + "** roles on **" + event.getGuild().getName() + "**");
        Boolean permissionOverride = event.getPermissionService().getPermissionPermission(user, options.getNode())
                .orElse(null);
        boolean permissionPermission = event.getPermissionService().hasPermissionPermission(user, options.getNode());
        append(builder, permissionOverride, permissionPermission,
                "**" + user.getEffectiveName() + "** permissions on **" + event.getGuild().getName() + "**");
        appendServerTrace(event, options, builder);
    }

    private void appendRoleTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        Boolean roleOverride = event.getPermissionService()
                .getRolePermission(options.getRole(), options.getNode()).orElse(null);
        boolean rolePermission = event.getPermissionService()
                .hasRolePermission(options.getRole(), options.getNode());
        append(builder, roleOverride, rolePermission,
                "**" + options.getRole().getName() + "** on **" + event.getGuild().getName() + "**");
        Boolean permissionOverride = event.getPermissionService()
                .getPermissionPermission(options.getRole(), options.getNode()).orElse(null);
        boolean permissionPermission = event.getPermissionService()
                .hasPermissionPermission(options.getRole(), options.getNode());
        append(builder, permissionOverride, permissionPermission,
                "**" + options.getRole().getName() + "** permissions on **" + event.getGuild().getName() + "**");
        appendServerTrace(event, options, builder);
    }

    private void appendPermissionTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        Boolean permissionOverride = event.getPermissionService()
                .getPermissionPermission(event.getGuild(), options.getPermission(), options.getNode()).orElse(null);
        boolean permissionPermission = event.getPermissionService()
                .hasPermissionPermission(event.getGuild(), options.getPermission(), options.getNode());
        append(builder, permissionOverride, permissionPermission,
                "**" + options.getPermission().getName() + "** on **" + event.getGuild().getName() + "**");
        appendServerTrace(event, options, builder);
    }

    private void appendServerTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        Boolean serverOverride = event.getPermissionService()
                .getServerPermission(event.getGuild(), options.getNode()).orElse(null);
        boolean serverPermission = event.getPermissionService()
                .hasServerPermission(event.getGuild(), options.getNode());
        append(builder, serverOverride, serverPermission, "*everyone* on **" + event.getGuild().getName() + "**");
    }

    private void append(StringBuilder builder, Boolean override, boolean permission, String scope) {
        builder.append('\n');
        if (override == null) {
            builder.append((permission ? CommandReaction.DEFAULT_OK : CommandReaction.DEFAULT_NOK).getUnicode());
        } else {
            builder.append((override ? CommandReaction.OK : CommandReaction.PROHIBITED).getUnicode());
        }
        builder.append(" → ");
        builder.append((permission ? CommandReaction.OK : CommandReaction.PROHIBITED).getUnicode());
        builder.append(" - ");
        builder.append(scope);
    }

}
