package de.nevini.command;

import de.nevini.modules.Module;
import de.nevini.util.Formatter;
import lombok.NonNull;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

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
            log.info("{} - Executing {} called via {}", event.getMessageId(), getClass().getSimpleName(),
                    summarize(event.getMessage().getContentRaw()));
            execute(event);
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
        return !isOwnerOnly() || event.isOwner();
    }

    private boolean checkChannel(CommandEvent event) {
        if (!event.isFromType(ChannelType.TEXT) && isGuildOnly()) {
            event.reply(CommandReaction.ERROR, "That command cannot be executed via direct message!");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkModule(CommandEvent event) {
        if (event.getModuleService().isModuleActive(event.getGuild(), getModule())) {
            return true;
        } else {
            event.reply(CommandReaction.DISABLED, "The **" + getModule().getName()
                    + "** module is disabled on **" + event.getGuild().getName() + "**!");
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
        if (!event.isFromType(ChannelType.TEXT) || event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), getMinimumBotPermissions())) {
            return true;
        } else {
            String[] missingPermissions = Arrays.stream(getMinimumBotPermissions())
                    .filter(permission -> !event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), permission))
                    .map(Permission::getName).toArray(String[]::new);
            if (missingPermissions.length == 0) {
                return true;
            } else if (missingPermissions.length == 1) {
                event.reply(CommandReaction.ERROR, "I need the **" + missingPermissions[0]
                        + "** permission to execute that command!");
                return false;
            } else {
                event.reply(CommandReaction.ERROR, "I need the **"
                        + Formatter.join(missingPermissions, "**, **", "** and **")
                        + "** permissions to execute that command!");
                return false;
            }
        }
    }

    private boolean checkUserPermission(CommandEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            final Optional<Boolean> permissionOverride = getNode() == null
                    ? Optional.empty()
                    : event.getPermissionService().hasPermission(event.getTextChannel(), event.getAuthor(), getNode());
            boolean permission = permissionOverride.orElseGet(() -> event.getMember().hasPermission(event.getTextChannel(), getNode().getDefaultPermissions()));
            if (permission) {
                String[] missingPermissions = Arrays.stream(getMinimumUserPermissions())
                        .filter(p -> !event.getMember().hasPermission(event.getTextChannel(), p))
                        .map(Permission::getName).toArray(String[]::new);
                if (missingPermissions.length == 0) {
                    return true;
                } else if (missingPermissions.length == 1) {
                    event.reply(CommandReaction.ERROR, "You need the **" + missingPermissions[0]
                            + "** permission to execute that command!");
                    return false;
                } else {
                    event.reply(CommandReaction.ERROR, "You need the **"
                            + Formatter.join(missingPermissions, "**, **", "** and **")
                            + "** permissions to execute that command!");
                    return false;
                }
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
