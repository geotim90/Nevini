package de.nevini.command;

import de.nevini.scope.Module;
import de.nevini.scope.Node;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandPatterns;
import de.nevini.util.command.CommandReaction;
import lombok.NonNull;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static de.nevini.util.Formatter.summarize;

@Slf4j
public abstract class Command {

    @Delegate
    private final CommandDescriptor descriptor;

    protected Command(@NonNull CommandDescriptor descriptor) {
        this.descriptor = descriptor;
        verify();
    }

    private void verify() {
        if (!getKeyword().matches(CommandPatterns.KEYWORD) || getKeyword().matches(CommandPatterns.OPTION)) {
            throw new IllegalStateException("Invalid keyword: " + getKeyword());
        }
        for (String alias : getAliases()) {
            if (!alias.matches(CommandPatterns.KEYWORD) || alias.matches(CommandPatterns.OPTION)) {
                throw new IllegalStateException("Invalid alias: " + alias);
            }
        }
        for (Command child : getChildren()) {
            if (child == null) {
                throw new IllegalStateException("Child is null");
            }
        }
        if (getDescription().isEmpty()) {
            throw new IllegalStateException("Description is empty");
        }
    }

    public void onEvent(CommandEvent event) {
        if (checkChild(event)
                && checkOwner(event)
                && checkChannel(event)
                && checkModule(event)
                && checkBotPermission(event)
                && checkUserPermission(event)
        ) {
            // count all command calls (including direct messages)
            event.getMetricsService().mark("Command executions");

            // count calls per command class (including direct messages)
            event.getMetricsService().mark("Command executions (" + getClass().getSimpleName() + ")");

            // count calls per guild (excluding direct messages)
            if (event.getGuild() != null) {
                log.info("{} {} {} - Executing {} called via {}", event.getGuild().getId(), event.getAuthor().getId(),
                        event.getMessageId(), getClass().getSimpleName(), summarize(event.getMessage().getContentRaw()));
                event.getMetricsService().mark("Command executions @ " + event.getGuild().getId());
            }

            try {
                execute(event);
            } catch (RuntimeException e) {
                log.warn("Command aborted with runtime exception: ", e);
            }
        }
    }

    private boolean checkChild(CommandEvent event) {
        if (StringUtils.isNotEmpty(event.getArgument())) {
            String[] args = event.getArgument().split("\\s+", 2);
            for (Command child : getChildren()) {
                if (child.isCommandFor(args[0])) {
                    child.onEvent(event.withArgument(args.length > 1 ? args[1] : null));
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isCommandFor(String keyword) {
        return StringUtils.isNotEmpty(keyword) && (keyword.equalsIgnoreCase(getKeyword())
                || Arrays.stream(getAliases()).anyMatch(keyword::equalsIgnoreCase));
    }

    private boolean checkOwner(CommandEvent event) {
        return !isOwnerOnly() || event.isBotOwner();
    }

    private boolean checkChannel(CommandEvent event) {
        if (!event.isFromType(ChannelType.TEXT) && isGuildOnly()) {
            event.reply(CommandReaction.ERROR, "You cannot use this command via direct message!", event::complete);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkModule(CommandEvent event) {
        if (event.getModuleService().isModuleActive(event.getGuild(), getModule())) {
            return true;
        } else {
            // only respond to privileged users
            if (event.isBotOwner() || event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                event.reply(CommandReaction.DISABLED, "The **" + getModule().getName()
                        + "** module is disabled on **" + event.getGuild().getName() + "**!", event::complete);
            }
            return false;
        }
    }

    public Module getModule() {
        if (getNode() == null) {
            return getChildren()[0].getModule();
        } else {
            return getNode().getModule();
        }
    }

    private boolean checkBotPermission(CommandEvent event) {
        if (!event.isFromType(ChannelType.TEXT)
                || event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), getMinimumBotPermissions())
        ) {
            return true;
        } else {
            String[] missingPermissions = Arrays.stream(getMinimumBotPermissions())
                    .filter(permission ->
                            !event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), permission))
                    .map(Permission::getName).toArray(String[]::new);
            if (missingPermissions.length == 0) {
                return true;
            } else if (missingPermissions.length == 1) {
                // only respond to privileged users
                if (event.isBotOwner() || isSuperiorUser(event)) {
                    event.reply(CommandReaction.ERROR, "I need the **" + missingPermissions[0]
                            + "** permission to execute that command!", event::complete);
                }
                return false;
            } else {
                // only respond to privileged users
                if (event.isBotOwner() || isSuperiorUser(event)) {
                    event.reply(CommandReaction.ERROR, "I need the **"
                            + Formatter.join(missingPermissions, "**, **", "** and **")
                            + "** permissions to execute that command!", event::complete);
                }
                return false;
            }
        }
    }

    private boolean checkUserPermission(CommandEvent event) {
        boolean result = getNode() == null || checkUserNodePermission(event, getNode());
        if (!result) {
            // only respond to privileged users
            if (event.isBotOwner() || isSuperiorUser(event)) {
                event.reply(CommandReaction.PROHIBITED,
                        "You do not have permission to execute that command.", event::complete);
            }
        }
        return result;
    }

    /**
     * Checks whether the current user has access to the provided {@link Node}.
     */
    protected boolean checkUserNodePermission(@NonNull CommandEvent event, @NonNull Node node) {
        if (event.isFromType(ChannelType.TEXT)) {
            // check if user has been grated access to node or has default permissions
            return event.isBotOwner() || event.getPermissionService().hasChannelUserPermission(
                    event.getTextChannel(), event.getMember(), node
            );
        } else {
            // no guild to apply restrictions with
            return true;
        }
    }

    /**
     * Returns {@code true} if {@link CommandEvent#getMember()} can change the permissions of the bot in this channel.
     *
     * @param event if {@link CommandEvent}
     * @throws NullPointerException if {@code event} or {@link CommandEvent#getMember()} is {@code null}
     */
    private boolean isSuperiorUser(@NonNull CommandEvent event) {
        return event.getMember().hasPermission(event.getTextChannel(), Permission.MANAGE_PERMISSIONS)
                && event.getMember().canInteract(event.getGuild().getSelfMember());
    }

    protected abstract void execute(CommandEvent event);

}