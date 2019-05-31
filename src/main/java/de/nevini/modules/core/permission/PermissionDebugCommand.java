package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import org.apache.commons.lang3.ObjectUtils;

public class PermissionDebugCommand extends Command {

    public PermissionDebugCommand() {
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
        StringBuilder builder = new StringBuilder("Permission to execute commands with permission node **" + options.getNode().getNode() + "**\n");
        if (options.isServer()) {
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
        boolean channelUserPermission = event.getPermissionService().hasChannelUserPermission(channel, user, options.getNode());
        append(builder, channelUserPermission, "__**" + user.getEffectiveName() + "** in **" + channel.getName() + "**__");
        boolean channelRolePermission = event.getPermissionService().hasChannelRolePermission(channel, user, options.getNode());
        append(builder, channelRolePermission, "**" + user.getEffectiveName() + "** roles in **" + channel.getName() + "**");
        boolean channelPermissionPermission = event.getPermissionService().hasChannelPermissionPermission(channel, user, options.getNode());
        append(builder, channelPermissionPermission, "**" + user.getEffectiveName() + "** permissions in **" + channel.getName() + "**");
        boolean channelPermission = event.getPermissionService().hasChannelPermission(channel, options.getNode());
        append(builder, channelPermission, "*everyone* in **" + channel.getName() + "**");
        appendUserTrace(event, options, builder);
    }

    private void appendChannelRoleTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        TextChannel channel = ObjectUtils.defaultIfNull(options.getChannel(), event.getTextChannel());
        boolean channelRolePermission = event.getPermissionService().hasChannelRolePermission(channel, options.getRole(), options.getNode());
        append(builder, channelRolePermission, "**" + options.getRole().getName() + "** in **" + channel.getName() + "**");
        boolean channelPermissionPermission = event.getPermissionService().hasChannelPermissionPermission(channel, options.getRole(), options.getNode());
        append(builder, channelPermissionPermission, "**" + options.getRole().getName() + "** permissions in **" + channel.getName() + "**");
        boolean channelPermission = event.getPermissionService().hasChannelPermission(channel, options.getNode());
        append(builder, channelPermission, "*everyone* in **" + channel.getName() + "**");
        appendRoleTrace(event, options, builder);
    }

    private void appendChannelPermissionTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        TextChannel channel = ObjectUtils.defaultIfNull(options.getChannel(), event.getTextChannel());
        boolean channelPermissionPermission = event.getPermissionService().hasChannelPermissionPermission(channel, options.getPermission(), options.getNode());
        append(builder, channelPermissionPermission, "**" + options.getPermission().getName() + "** in **" + channel.getName() + "**");
        boolean channelPermission = event.getPermissionService().hasChannelPermission(channel, options.getNode());
        append(builder, channelPermission, "*everyone* in **" + channel.getName() + "**");
        appendPermissionTrace(event, options, builder);
    }

    private void appendChannelTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        boolean channelPermission = event.getPermissionService().hasChannelPermission(options.getChannel(), options.getNode());
        append(builder, channelPermission, "*everyone* in **" + options.getChannel().getName() + "**");
        appendServerTrace(event, options, builder);
    }

    private void appendUserTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        Member user = ObjectUtils.defaultIfNull(options.getMember(), event.getMember());
        boolean userPermission = event.getPermissionService().hasUserPermission(user, options.getNode());
        append(builder, userPermission, "**" + user.getEffectiveName() + "** on **" + event.getGuild().getName() + "**");
        boolean rolePermission = event.getPermissionService().hasRolePermission(user, options.getNode());
        append(builder, rolePermission, "**" + user.getEffectiveName() + "** roles on **" + event.getGuild().getName() + "**");
        boolean permissionPermission = event.getPermissionService().hasPermissionPermission(user, options.getNode());
        append(builder, permissionPermission, "**" + user.getEffectiveName() + "** permissions on **" + event.getGuild().getName() + "**");
        appendServerTrace(event, options, builder);
    }

    private void appendRoleTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        boolean rolePermission = event.getPermissionService().hasRolePermission(options.getRole(), options.getNode());
        append(builder, rolePermission, "**" + options.getRole().getName() + "** on **" + event.getGuild().getName() + "**");
        boolean permissionPermission = event.getPermissionService().hasPermissionPermission(options.getRole(), options.getNode());
        append(builder, permissionPermission, "**" + options.getRole().getName() + "** permissions on **" + event.getGuild().getName() + "**");
        appendServerTrace(event, options, builder);
    }

    private void appendPermissionTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        boolean permissionPermission = event.getPermissionService().hasPermissionPermission(event.getGuild(), options.getPermission(), options.getNode());
        append(builder, permissionPermission, "**" + options.getPermission().getName() + "** on **" + event.getGuild().getName() + "**");
        appendServerTrace(event, options, builder);
    }

    private void appendServerTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        boolean serverPermission = event.getPermissionService().hasServerPermission(event.getGuild(), options.getNode());
        append(builder, serverPermission, "*everyone* on **" + event.getGuild().getName() + "**");
    }

    private void append(StringBuilder builder, boolean permission, String scope) {
        builder.append('\n');
        builder.append(permission ? CommandReaction.OK.getUnicode() : CommandReaction.PROHIBITED.getUnicode());
        builder.append(" - ");
        builder.append(scope);
    }

}
