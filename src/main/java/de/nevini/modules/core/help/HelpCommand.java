package de.nevini.modules.core.help;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.scope.Module;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.util.Formatter;
import net.dv8tion.jda.core.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;

@Component
public class HelpCommand extends Command {

    public HelpCommand() {
        super(CommandDescriptor.builder()
                .keyword("help")
                .aliases(new String[]{"about", "info"})
                .guildOnly(false)
                .node(Node.CORE_HELP)
                .minimumBotPermissions(Permissions.NONE)
                .description("provides a list of commands or details on a specific command")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("[<command>]")
                                .description("the specific command to look up")
                                .keyword("command")
                                .build()
                })
                .details("If a valid command is provided, this will display details on that specific command.\n"
                        + "If no valid command is provided, this will display a list of commands.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        if (StringUtils.isEmpty(event.getArgument())) {
            doCommandList(event);
        } else {
            String[] args = event.getArgument().split("\\s+");
            Command command = event.getCommands().get(args[0].toLowerCase());
            doCommandHelp(event.withArgument(args.length > 1 ? args[1] : null), command, command.getKeyword());
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
        for (Module module : Module.values()) {
            if (event.getModuleService().isModuleActive(event.getGuild(), module)) {
                builder.append("\n\n__Module: **").append(module.getName()).append("**__");
                event.getCommands().values().stream()
                        .filter(command -> module.equals(command.getModule())
                                && (!command.isOwnerOnly() || event.isBotOwner()))
                        .sorted(Comparator.comparing(Command::getKeyword)).distinct()
                        .forEach(command -> builder.append("\n**")
                                .append(command.getKeyword()).append("** - ").append(command.getDescription()));
            }
        }
        builder.append("\n\nTo add me to your server, visit <")
                .append(event.getJDA().asBot().getInviteUrl(Permissions.BOT_FULL)).append(">");
        builder.append("\n\nFurther documentation can be viewed under <https://nevini.de/docs>")
                .append("\n\nFor additional help and support, join ").append(event.getServerInvite());
        event.replyDm(builder.toString(), ignore -> event.complete(true));
    }

    private void doCommandHelp(CommandEvent event, Command command, String chain) {
        if (command == null) {
            doCommandList(event);
            return;
        } else if (StringUtils.isNotEmpty(event.getArgument())) {
            String[] args = event.getArgument().split("\\s+", 2);
            for (Command child : command.getChildren()) {
                if (child.isCommandFor(args[0])) {
                    doCommandHelp(event.withArgument(args.length > 1 ? args[1] : null), child,
                            chain + ' ' + child.getKeyword());
                    return;
                }
            }
        }

        if (!command.isOwnerOnly() || event.isBotOwner()) {
            // command header
            StringBuilder builder = new StringBuilder("__Command__\n**" + chain + "** - " + command.getDescription());

            // details
            if (StringUtils.isNotEmpty(command.getDetails())) {
                builder.append("\n\n").append(command.getDetails());
            }

            // owner only
            builder.append('\n');
            if (command.isOwnerOnly()) {
                builder.append("\nThis command can only be executed by the owner of the bot.");
            }
            // guild only
            if (command.isGuildOnly()) {
                builder.append("\nThis command can only be executed in a text channel and not via direct message.");
            } else {
                builder.append("\nThis command can be executed in a text channel or via direct message.");
            }

            // permission node
            if (command.getNode() != null) {
                String[] permissions = Arrays.stream(command.getNode().getDefaultPermissions())
                        .map(Permission::getName).toArray(String[]::new);
                builder.append("\n");
                if (permissions.length > 0) {
                    builder.append("\nBy default, you need the **");
                    if (permissions.length == 1) {
                        builder.append(permissions[0]).append("** permission");
                    } else {
                        builder.append(Formatter.join(permissions, "**, **", "** and **"))
                                .append("** permissions");
                    }
                    builder.append(" to execute this command.");
                }
                builder.append("\nThis command belongs to the **").append(command.getModule().getName())
                        .append("** module. Permission overrides may be applied on node **")
                        .append(command.getNode().getNode()).append("**.");
            }

            // option list
            if (command.getOptions().length > 0) {
                builder.append("\n\n__Options__");
                for (CommandOptionDescriptor option : command.getOptions()) {
                    builder.append("\n**").append(option.getSyntax()).append("** - ")
                            .append(option.getDescription().replace('\n', ' '));
                }
            }

            // aliases
            if (command.getAliases().length > 0
                    || Arrays.stream(command.getOptions()).anyMatch(e -> e.getAliases().length > 0)
            ) {
                builder.append("\n\n__Aliases__");
                if (command.getAliases().length > 0) {
                    builder.append("\nYou can also use **")
                            .append(Formatter.join(command.getAliases(), "**, **", "** or **"))
                            .append("** instead of **").append(command.getKeyword()).append("**");
                }
                for (CommandOptionDescriptor option : command.getOptions()) {
                    if (option.getAliases().length > 0) {
                        builder.append("\nYou can also use **")
                                .append(Formatter.join(option.getAliases(), "**, **", "** or **"))
                                .append("** instead of **").append(option.getKeyword()).append("**");
                    }
                }
            }

            // children
            if (command.getChildren().length > 0) {
                builder.append("\n\n__Commands__");
                for (Command child : command.getChildren()) {
                    builder.append("\n**").append(chain).append(' ').append(child.getKeyword()).append("** - ")
                            .append(child.getDescription());
                }
            }

            event.replyDm(builder.toString(), ignore -> event.complete(true));
        } else {
            doCommandList(event);
        }
    }

}
