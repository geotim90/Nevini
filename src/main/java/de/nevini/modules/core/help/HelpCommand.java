package de.nevini.modules.core.help;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import de.nevini.util.Formatter;
import de.nevini.util.Permissions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class HelpCommand extends Command {

    public HelpCommand() {
        super(CommandDescriptor.builder()
                .keyword("help")
                .guildOnly(false)
                .node(Node.CORE_HELP)
                .minimumBotPermissions(Permissions.NONE)
                .minimumUserPermissions(Permissions.NONE)
                .description("provides a list of commands or details on a specific command")
                .syntax("[<command>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        if (StringUtils.isEmpty(event.getArgument())) {
            doCommandList(event);
        } else {
            String[] args = event.getArgument().split("\\s+");
            Command command = event.getCommands().get(args[0].toLowerCase());
            doCommandHelp(event.withArgument(args.length > 1 ? args[1] : null),
                    event.getPrefixService().getGuildPrefix(event.getGuild()), command);
        }
    }

    private void doCommandList(CommandEvent event) {
        StringBuilder builder = new StringBuilder();
        if (event.getGuild() == null) {
            builder.append("Here is a list of all **").append(event.getJDA().getSelfUser().getName())
                    .append("** commands.");
        } else {
            builder.append("Here is a list of **").append(event.getJDA().getSelfUser().getName())
                    .append("** commands for modules active on **").append(event.getGuild().getName()).append("**.");
        }
        String prefix = event.getPrefixService().getGuildPrefix(event.getGuild());
        for (Module module : Module.values()) {
            if (event.getModuleService().isModuleActive(event.getGuild(), module)) {
                builder.append("\n\n__Module: **").append(module.getName()).append("**__");
                event.getCommands().values().stream()
                        .filter(command -> module.equals(command.getModule())
                                && (!command.isOwnerOnly() || event.isOwner()))
                        .sorted(Comparator.comparing(Command::getKeyword)).distinct()
                        .forEach(command -> builder.append("\n").append(prefix).append(" **")
                                .append(command.getKeyword()).append("** - ").append(command.getDescription()));
            }
        }
        builder.append("\n\nTo add me to your server, visit " +
                "<https://discordapp.com/oauth2/authorize?client_id=570972920692736002&scope=bot&permissions=519232>");
        builder.append("\n\nFor additional help and support, join ").append(event.getServerInvite());
        event.replyDm(builder.toString(), ignore -> event.complete(true));
    }

    private void doCommandHelp(CommandEvent event, String chain, Command command) {
        if (command == null) {
            doCommandList(event);
            return;
        } else if (StringUtils.isNotEmpty(event.getArgument())) {
            String[] args = event.getArgument().split("\\s+", 2);
            for (Command child : command.getChildren()) {
                if (child.isCommandFor(args[0])) {
                    doCommandHelp(event.withArgument(args.length > 1 ? args[1] : null),
                            chain + ' ' + command.getKeyword(), child);
                    return;
                }
            }
        }

        if (!command.isOwnerOnly() || event.isOwner()) {
            StringBuilder builder = new StringBuilder("__Module: **" + command.getModule().getName() + "**__\n"
                    + chain + " **" + command.getKeyword() + "** - " + command.getDescription());
            if (StringUtils.isNotEmpty(command.getSyntax())) {
                builder.append("\n\n__Usage__\n```").append(chain).append(" ").append(command.getKeyword()).append(" ")
                        .append(command.getSyntax()).append("```");
            }
            if (StringUtils.isNotEmpty(command.getDetails())) {
                builder.append("\n\n").append(command.getDetails());
            }
            if (command.getAliases().length > 0) {
                builder.append("\n\n__Aliases__\n").append("You can also use **")
                        .append(Formatter.join(command.getAliases(), "**, **", "** or **"))
                        .append("** instead of **").append(command.getKeyword()).append("**");
            }
            if (command.getChildren().length > 0) {
                builder.append("\n\n__Nested commands__");
                for (Command child : command.getChildren()) {
                    builder.append("\n").append(chain).append(" ").append(command.getKeyword()).append(" **")
                            .append(child.getKeyword()).append("** - ").append(child.getDescription());
                }
            }
            event.replyDm(builder.toString(), ignore -> event.complete(true));
        } else {
            doCommandList(event);
        }
    }

}
