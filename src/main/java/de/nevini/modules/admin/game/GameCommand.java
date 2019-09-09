package de.nevini.modules.admin.game;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GameCommand extends Command {

    private static final Pattern PATTERN = Pattern.compile("(?i)(id|name)=(.*)\\s+map\\s+(id|name|icon|multi|reject)=(.*)");

    public GameCommand() {
        super(CommandDescriptor.builder()
                .keyword("game")
                .ownerOnly(true)
                .guildOnly(false)
                .node(Node.CORE_HELP) // dummy node
                .minimumBotPermissions(Permissions.TALK)
                .description("configures rich presence mappings")
                .details("Syntax: `( id=<id> | name=<name> ) map ( id=<id> | name=<name> | icon=<url> | multi=(true|false) | reject=(true|false) )`")
                .build());
    }

    @Override
    public void execute(CommandEvent event) {
        // check syntax
        Matcher matcher = PATTERN.matcher(event.getArgument());
        if (!matcher.matches()) {
            event.reply(CommandReaction.WARNING, "Invalid syntax!", event::complete);
            return;
        }

        // parse input
        String keyType = matcher.group(1);
        String key = matcher.group(2);
        String valueType = matcher.group(3);
        String value = matcher.group(4);

        if ("name".equalsIgnoreCase(keyType)) {
            if ("id".equalsIgnoreCase(valueType)) {
                try {
                    event.getGameService().mapIdByName(Long.parseUnsignedLong(value), key);
                    event.reply(CommandReaction.OK, event::complete);
                } catch (NumberFormatException ignore) {
                    event.reply(CommandReaction.WARNING, "Invalid id!", event::complete);
                }
            } else {
                event.reply(CommandReaction.WARNING, "Invalid value type!", event::complete);
            }
        } else if ("id".equalsIgnoreCase(keyType)) {
            long id;
            try {
                id = Long.parseUnsignedLong(key);
            } catch (NumberFormatException ignore) {
                event.reply(CommandReaction.WARNING, "Invalid id!", event::complete);
                return;
            }
            if ("name".equalsIgnoreCase(valueType)) {
                event.getGameService().mapNameById(value, id);
                event.reply(CommandReaction.OK, event::complete);
            } else if ("icon".equalsIgnoreCase(valueType)) {
                event.getGameService().mapIconById(value, id);
                event.reply(CommandReaction.OK, event::complete);
            } else if ("multi".equalsIgnoreCase(valueType)) {
                event.getGameService().mapMultiById(Boolean.parseBoolean(value), id);
                event.reply(CommandReaction.OK, event::complete);
            } else if ("reject".equalsIgnoreCase(valueType)) {
                event.getGameService().mapRejectById(Boolean.parseBoolean(value), id);
                event.reply(CommandReaction.OK, event::complete);
            } else {
                event.reply(CommandReaction.WARNING, "Invalid value type!", event::complete);
            }
        } else {
            event.reply(CommandReaction.WARNING, "Invalid key type!", event::complete);
        }
    }

}
