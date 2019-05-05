package de.nevini.command;

import de.nevini.util.FormatUtils;
import lombok.NonNull;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

import static de.nevini.util.FormatUtils.summarize;

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
            log.info("{} - Executing {} called via {}", event.getMessageId(), getClass().getSimpleName(),
                    summarize(event.getMessage().getContentRaw()));
            execute(event);
        }
    }

    protected boolean checkChild(CommandEvent event) {
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

    protected boolean checkOwner(CommandEvent event) {
        return !isOwnerOnly() || event.isOwner();
    }

    protected boolean checkChannel(CommandEvent event) {
        if (!event.isFromType(ChannelType.TEXT) && isGuildOnly()) {
            event.reply(CommandReaction.ERROR, "That command cannot be executed via direct message!");
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkModule(CommandEvent event) {
        if (event.getModuleService().isModuleActive(event.getGuild(), getModule())) {
            return true;
        } else {
            event.reply(CommandReaction.DISABLED, "The **" + getModule().getName()
                    + "** module is disabled on this server!");
            return false;
        }
    }

    protected boolean checkBotPermission(CommandEvent event) {
        if (!event.isFromType(ChannelType.TEXT) || event.getMember().hasPermission(event.getTextChannel(),
                getMinimumBotPermissions())) {
            return true;
        } else {
            String[] missingPermissions = Arrays.stream(getMinimumBotPermissions())
                    .filter(permission -> !event.getMember().hasPermission(permission))
                    .map(Permission::getName).toArray(String[]::new);
            if (missingPermissions.length == 0) {
                return true;
            } else if (missingPermissions.length == 1) {
                event.reply(CommandReaction.ERROR, "I need the **" + missingPermissions[0]
                        + "** permission to execute that command!");
                return false;
            } else {
                event.reply(CommandReaction.ERROR, "I need the **"
                        + FormatUtils.join(missingPermissions, "**, **", "** and **")
                        + "** permissions to execute that command!");
                return false;
            }
        }
    }

    protected boolean checkUserPermission(CommandEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            final Optional<Boolean> permissionOverride = getNode() == null ? Optional.empty() :
                    event.getPermissionService().hasPermission(event.getTextChannel(), event.getAuthor(),
                            getNode().getNode());
            boolean permission = permissionOverride.orElseGet(() ->
                    event.getMember().hasPermission(event.getTextChannel(), getMinimumUserPermissions()));
            if (permission) {
                return true;
            } else {
                event.reply(CommandReaction.PROHIBITED, "You do not have permission to execute that command.");
                return false;
            }
        } else {
            return true;
        }
    }

    protected abstract void execute(CommandEvent event);

}
