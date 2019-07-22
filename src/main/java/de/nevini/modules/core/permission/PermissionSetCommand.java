package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.util.command.CommandReaction;
import de.nevini.scope.Node;
import de.nevini.util.message.ConfirmationDecorator;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.TextChannel;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

abstract class PermissionSetCommand extends Command {

    private final String verb;
    private final Boolean override;

    PermissionSetCommand(
            @NonNull CommandDescriptor commandDescriptor, @NonNull String verb, Boolean override
    ) {
        super(commandDescriptor);
        this.verb = verb;
        this.override = override;
    }

    @Override
    protected void execute(CommandEvent event) {
        new PermissionOptions(event, true, true, options -> acceptOptions(event, options)).get();
    }

    private void acceptOptions(CommandEvent event, PermissionOptions options) {
        if (canSetPermission(event, options)) {
            if (options.isServer()) {
                if (options.getPermission() != null) {
                    setPermissionPermissions(event, options);
                } else if (options.getRole() != null) {
                    setRolePermissions(event, options);
                } else if (options.getMember() != null) {
                    setUserPermissions(event, options);
                } else {
                    setServerPermissions(event, options);
                }
            } else {
                if (options.getPermission() != null) {
                    setChannelPermissionPermissions(event, options);
                } else if (options.getRole() != null) {
                    setChannelRolePermissions(event, options);
                } else if (options.getMember() != null) {
                    setChannelUserPermissions(event, options);
                } else if (options.getChannel() != null) {
                    setChannelPermissions(event, options);
                } else {
                    setChannelUserPermissions(event, options);
                }
            }
        }
    }

    private boolean canSetPermission(CommandEvent event, PermissionOptions options) {
        if (options.isServer()) {
            if (options.getNodes().stream()
                    .anyMatch(node -> !event.getPermissionService().hasUserPermission(event.getMember(), node))
            ) {
                event.reply(CommandReaction.PROHIBITED,
                        "You can only configure server-level permissions for permission nodes you have yourself!",
                        event::complete);
                return false;
            } else if (options.getPermission() != null) {
                if (!event.getMember().hasPermission(options.getPermission())) {
                    event.reply(
                            CommandReaction.PROHIBITED,
                            "You can only configure server-level permissions "
                                    + "for Discord permissions you have yourself!",
                            event::complete
                    );
                    return false;
                }
            }
        } else {
            TextChannel channel = ObjectUtils.defaultIfNull(options.getChannel(), event.getTextChannel());
            if (options.getNodes().stream().anyMatch(node ->
                    !event.getPermissionService().hasChannelUserPermission(channel, event.getMember(), node))
            ) {
                event.reply(CommandReaction.PROHIBITED,
                        "You can only configure channel-level permissions "
                                + "for permission nodes you have yourself!",
                        event::complete);
                return false;
            } else if (options.getPermission() != null) {
                if (!event.getMember().hasPermission(channel, options.getPermission())) {
                    event.reply(CommandReaction.PROHIBITED, "You can only configure channel-level permissions "
                            + "for Discord permissions you have yourself!", event::complete);
                    return false;
                }
            }
        }
        if (options.getRole() != null) {
            if (!event.getMember().canInteract(options.getRole())) {
                event.reply(CommandReaction.PROHIBITED, "You can only configure permissions "
                        + "for roles of a lower position than your highest role!", event::complete);
                return false;
            }
        } else if (options.getMember() != null) {
            if (!event.getMember().canInteract(options.getMember())) {
                event.reply(CommandReaction.PROHIBITED, "You can only configure permissions "
                        + "for users whose highest role is lower than your highest role!", event::complete);
                return false;
            }
        }
        return true;
    }

    private void setServerPermissions(CommandEvent event, PermissionOptions options) {
        StringBuilder builder = new StringBuilder("Are you sure you want to __" + verb
                + "__ the following permissions " + "for *everyone* on **" + event.getGuild().getName() + "**?\n");
        appendNodes(builder, options.getNodes());
        confirmPermissions(event, builder.toString(), () -> {
            for (Node node : options.getNodes()) {
                event.getPermissionService().setServerPermission(event.getGuild(), node, override);
            }
            event.reply(CommandReaction.OK, event::complete);
        });
    }

    private void setPermissionPermissions(CommandEvent event, PermissionOptions options) {
        StringBuilder builder = new StringBuilder("Are you sure you want to __" + verb
                + "__ the following permissions " + "for **" + options.getPermission().getName() + "** on **"
                + event.getGuild().getName() + "**?\n");
        appendNodes(builder, options.getNodes());
        confirmPermissions(event, builder.toString(), () -> {
            for (Node node : options.getNodes()) {
                event.getPermissionService()
                        .setPermissionPermission(event.getGuild(), options.getPermission(), node, override);
            }
            event.reply(CommandReaction.OK, event::complete);
        });
    }

    private void setRolePermissions(CommandEvent event, PermissionOptions options) {
        StringBuilder builder = new StringBuilder("Are you sure you want to __" + verb
                + "__ the following permissions " + "for **" + options.getRole().getName() + "** on **"
                + event.getGuild().getName() + "**?\n");
        appendNodes(builder, options.getNodes());
        confirmPermissions(event, builder.toString(), () -> {
            for (Node node : options.getNodes()) {
                event.getPermissionService().setRolePermission(options.getRole(), node, override);
            }
            event.reply(CommandReaction.OK, event::complete);
        });
    }

    private void setUserPermissions(CommandEvent event, PermissionOptions options) {
        StringBuilder builder = new StringBuilder("Are you sure you want to __" + verb
                + "__ the following permissions " + "for **" + options.getMember().getEffectiveName() + "** on **"
                + event.getGuild().getName() + "**?\n");
        appendNodes(builder, options.getNodes());
        confirmPermissions(event, builder.toString(), () -> {
            for (Node node : options.getNodes()) {
                event.getPermissionService().setUserPermission(options.getMember(), node, override);
            }
            event.reply(CommandReaction.OK, event::complete);
        });
    }

    private void setChannelPermissions(CommandEvent event, PermissionOptions options) {
        StringBuilder builder = new StringBuilder("Are you sure you want to __" + verb
                + "__ the following permissions " + "for *everyone* in **" + event.getChannel().getName() + "**?\n");
        appendNodes(builder, options.getNodes());
        confirmPermissions(event, builder.toString(), () -> {
            for (Node node : options.getNodes()) {
                event.getPermissionService().setChannelPermission(options.getChannel(), node, override);
            }
            event.reply(CommandReaction.OK, event::complete);
        });
    }

    private void setChannelPermissionPermissions(CommandEvent event, PermissionOptions options) {
        StringBuilder builder = new StringBuilder("Are you sure you want to __" + verb
                + "__ the following permissions " + "for **" + options.getPermission().getName() + "** in **"
                + event.getChannel().getName() + "**?\n");
        appendNodes(builder, options.getNodes());
        confirmPermissions(event, builder.toString(), () -> {
            for (Node node : options.getNodes()) {
                event.getPermissionService()
                        .setChannelPermissionPermission(options.getChannel(), options.getPermission(), node, override);
            }
            event.reply(CommandReaction.OK, event::complete);
        });
    }

    private void setChannelRolePermissions(CommandEvent event, PermissionOptions options) {
        StringBuilder builder = new StringBuilder("Are you sure you want to __" + verb
                + "__ the following permissions " + "for **" + options.getRole().getName() + "** in **"
                + event.getChannel().getName() + "**?\n");
        appendNodes(builder, options.getNodes());
        confirmPermissions(event, builder.toString(), () -> {
            for (Node node : options.getNodes()) {
                event.getPermissionService()
                        .setChannelRolePermission(options.getChannel(), options.getRole(), node, override);
            }
            event.reply(CommandReaction.OK, event::complete);
        });
    }

    private void setChannelUserPermissions(CommandEvent event, PermissionOptions options) {
        TextChannel channel = ObjectUtils.defaultIfNull(options.getChannel(), event.getTextChannel());
        StringBuilder builder = new StringBuilder("Are you sure you want to __" + verb
                + "__ the following permissions " + "for **" + options.getMember().getEffectiveName() + "** in **"
                + channel.getName() + "**?\n");
        appendNodes(builder, options.getNodes());
        confirmPermissions(event, builder.toString(), () -> {
            for (Node node : options.getNodes()) {
                event.getPermissionService().setChannelUserPermission(channel, options.getMember(), node, override);
            }
            event.reply(CommandReaction.OK, event::complete);
        });
    }

    private void appendNodes(StringBuilder builder, List<Node> nodes) {
        for (Node node : nodes) {
            builder.append("\n**").append(node.getNode()).append("**");
        }
    }

    private void confirmPermissions(CommandEvent event, String content, Runnable callback) {
        event.replyNoDm(content, message -> new ConfirmationDecorator(
                message, event.getAuthor(), event.getEventDispatcher(),
                response -> {
                    if (Boolean.TRUE.equals(response)) {
                        callback.run();
                    } else {
                        event.reply(CommandReaction.DEFAULT_NOK, event::complete);
                    }
                }
        ).decorate());
    }

}
