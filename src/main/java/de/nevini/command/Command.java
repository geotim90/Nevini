package de.nevini.command;

import de.nevini.util.Emote;
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
        validate();
    }

    protected void validate() {
        if (!getKeyword().matches("[a-z0-9-]{1,32}")) {
            throw new IllegalStateException("Keyword does not match regex.");
        }
        for (String alias : getAliases()) {
            if (!alias.matches("[a-z0-9-]{1,32}")) {
                throw new IllegalStateException("Alias does not match regex.");
            }
        }
        for (Command child : getChildren()) {
            if (child == null) {
                throw new IllegalStateException("Child is null.");
            }
        }
        if (getDescription().isEmpty()) {
            throw new IllegalStateException("Description is empty.");
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
            log.info("{} - Executing {} with argument {}", event.getMessageId(), getClass().getSimpleName(),
                    summarize(event.getArgument()));
            execute(event);
        }
    }

    protected boolean checkChild(CommandEvent event) {
        if (StringUtils.isNotEmpty(event.getArgument())) {
            String[] args = event.getArgument().split("\\s+", 2);
            for (Command child : getChildren()) {
                if (child.isCommandFor(args[0])) {
                    event.setArgument(args.length > 1 ? args[1] : null);
                    child.onEvent(event);
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
            event.reply(Emote.ERROR, "That command cannot be executed via direct message!");
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkModule(CommandEvent event) {
        if (event.getModuleService().isModuleActive(event.getGuild(), getModule())) {
            return true;
        } else {
            event.reply(Emote.DISABLED, "The **" + getModule().getName()
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
                event.reply(Emote.ERROR, "I need the **" + missingPermissions[0]
                        + "** permission to execute that command!");
                return false;
            } else {
                event.reply(Emote.ERROR, "I need the **"
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
                event.reply(Emote.PROHIBITED, "You do not have permission to execute that command.");
                return false;
            }
        } else {
            return true;
        }
    }

    protected abstract void execute(CommandEvent event);

}
