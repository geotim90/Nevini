package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;

public class PermissionDebugCommand extends Command {

    public PermissionDebugCommand() {
        super(CommandDescriptor.builder()
                .keyword("debug")
                .node(Node.CORE_PERMISSION_DEBUG)
                .description("displays permission trace for bot commands")
                .syntax("[--node] <node> [<options>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        new PermissionOptions(event, true, false, options -> acceptOptions(event, options)).get();
    }

    private void acceptOptions(CommandEvent event, PermissionOptions options) {
        StringBuilder builder = new StringBuilder();
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
            if (options.getChannel() == null) {
                options.setChannel(event.getTextChannel());
            }
            if (options.getPermission() != null) {
                appendChannelPermissionTrace(event, options, builder);
            } else if (options.getRole() != null) {
                appendChannelRoleTrace(event, options, builder);
            } else if (options.getMember() != null) {
                appendChannelUserTrace(event, options, builder);
            } else {
                appendChannelTrace(event, options, builder);
            }
        }
        event.reply(builder.toString(), ignore -> event.complete());
    }

    private void appendChannelUserTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        boolean channelUserPermission = event.getPermissionService().hasChannelUserPermission(options.getChannel(), options.getMember(), options.getNode());
        append(builder, channelUserPermission, "__**" + options.getMember().getEffectiveName() + "** in **" + options.getChannel().getName() + "**__");
        boolean channelRolePermission = event.getPermissionService().hasChannelRolePermission(options.getChannel(), options.getMember(), options.getNode());
        append(builder, channelRolePermission, "**" + options.getMember().getEffectiveName() + "** roles in **" + options.getChannel().getName() + "**");
        boolean channelPermissionPermission = event.getPermissionService().hasChannelPermissionPermission(options.getChannel(), options.getMember(), options.getNode());
        append(builder, channelPermissionPermission, "**" + options.getMember().getEffectiveName() + "** permissions in **" + options.getChannel().getName() + "**");
        boolean channelPermission = event.getPermissionService().hasChannelPermission(options.getChannel(), options.getNode());
        append(builder, channelPermission, "*everyone* in **" + options.getChannel().getName() + "**");
        appendUserTrace(event, options, builder);
    }

    private void appendChannelRoleTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        boolean channelRolePermission = event.getPermissionService().hasChannelRolePermission(options.getChannel(), options.getRole(), options.getNode());
        append(builder, channelRolePermission, "**" + options.getRole().getName() + "** in **" + options.getChannel().getName() + "**");
        boolean channelPermissionPermission = event.getPermissionService().hasChannelPermissionPermission(options.getChannel(), options.getRole(), options.getNode());
        append(builder, channelPermissionPermission, "**" + options.getRole().getName() + "** permissions in **" + options.getChannel().getName() + "**");
        boolean channelPermission = event.getPermissionService().hasChannelPermission(options.getChannel(), options.getNode());
        append(builder, channelPermission, "*everyone* in **" + options.getChannel().getName() + "**");
        appendRoleTrace(event, options, builder);
    }

    private void appendChannelPermissionTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        boolean channelPermissionPermission = event.getPermissionService().hasChannelPermissionPermission(options.getChannel(), options.getPermission(), options.getNode());
        append(builder, channelPermissionPermission, "**" + options.getPermission().getName() + "** in **" + options.getChannel().getName() + "**");
        boolean channelPermission = event.getPermissionService().hasChannelPermission(options.getChannel(), options.getNode());
        append(builder, channelPermission, "*everyone* in **" + options.getChannel().getName() + "**");
        appendPermissionTrace(event, options, builder);
    }

    private void appendChannelTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        boolean channelPermission = event.getPermissionService().hasChannelPermission(options.getChannel(), options.getNode());
        append(builder, channelPermission, "*everyone* in **" + options.getChannel().getName() + "**");
        appendServerTrace(event, options, builder);
    }

    private void appendUserTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        boolean userPermission = event.getPermissionService().hasUserPermission(options.getMember(), options.getNode());
        append(builder, userPermission, "**" + options.getMember().getEffectiveName() + "**");
        boolean rolePermission = event.getPermissionService().hasRolePermission(options.getMember(), options.getNode());
        append(builder, rolePermission, "**" + options.getMember().getEffectiveName() + "** roles");
        boolean permissionPermission = event.getPermissionService().hasPermissionPermission(options.getMember(), options.getNode());
        append(builder, permissionPermission, "**" + options.getMember().getEffectiveName() + "** permissions");
        appendServerTrace(event, options, builder);
    }

    private void appendRoleTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        boolean rolePermission = event.getPermissionService().hasRolePermission(options.getRole(), options.getNode());
        append(builder, rolePermission, "**" + options.getRole().getName() + "**");
        boolean permissionPermission = event.getPermissionService().hasPermissionPermission(options.getRole(), options.getNode());
        append(builder, permissionPermission, "**" + options.getRole().getName() + "** permissions");
        appendServerTrace(event, options, builder);
    }

    private void appendPermissionTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        boolean permissionPermission = event.getPermissionService().hasPermissionPermission(event.getGuild(), options.getPermission(), options.getNode());
        append(builder, permissionPermission, "**" + options.getPermission().getName() + "**");
        appendServerTrace(event, options, builder);
    }

    private void appendServerTrace(CommandEvent event, PermissionOptions options, StringBuilder builder) {
        boolean serverPermission = event.getPermissionService().hasServerPermission(event.getGuild(), options.getNode());
        append(builder, serverPermission, "*everyone*");
    }

    private void append(StringBuilder builder, boolean permission, String scope) {
        builder.append(permission ? CommandReaction.OK.getUnicode() : CommandReaction.PROHIBITED.getUnicode());
        builder.append(" - ");
        builder.append(scope);
    }

}
