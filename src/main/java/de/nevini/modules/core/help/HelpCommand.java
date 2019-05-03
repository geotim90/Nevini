package de.nevini.modules.core.help;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import de.nevini.util.FormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand extends Command {

    public HelpCommand() {
        super(CommandDescriptor.builder()
                .keyword("help")
                .guildOnly(false)
                .module(Module.CORE)
                .node(Node.CORE_HELP)
                .description("provides a list of commands or details on specific commands")
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
            event.setArgument(args.length > 1 ? args[1] : null);
            doCommandHelp(event, event.getPrefixService().getGuildPrefix(event.getGuild()), command);
        }
    }

    // TODO group commands by module and sort alphabetically within group
    private void doCommandList(CommandEvent event) {
        StringBuilder builder = new StringBuilder("Here is a list of all **"
                + event.getJDA().getSelfUser().getName() + "** commands.");
        String prefix = event.getPrefixService().getGuildPrefix(event.getGuild());
        Module module = null;
        for (Command command : event.getCommands().values()) {
            if (!command.isOwnerOnly() || event.isOwner()) {
                if (!command.getModule().equals(module)) {
                    module = command.getModule();
                    builder.append("\n\n__Module: **").append(module.getName()).append("**__");
                }
                builder.append("\n").append(prefix).append(" **").append(command.getKeyword()).append("** - ")
                        .append(command.getDescription());
            }
        }
        builder.append("\n\nTo add me to your server, visit " +
                "<https://discordapp.com/oauth2/authorize?client_id=570972920692736002&scope=bot&permissions=519232>");
        builder.append("\n\nFor additional help and support, join ").append(event.getServerInvite());
        event.replyDm(builder.toString());
    }

    private void doCommandHelp(CommandEvent event, String chain, Command command) {
        if (command == null) {
            doCommandList(event);
            return;
        } else if (StringUtils.isNotEmpty(event.getArgument())) {
            String[] args = event.getArgument().split("\\s+", 2);
            for (Command child : getChildren()) {
                if (child.isCommandFor(args[0])) {
                    event.setArgument(args.length > 1 ? args[1] : null);
                    doCommandHelp(event, chain + ' ' + command.getKeyword(), child);
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
            if (command.getAliases().length > 0) {
                builder.append("\n\n__Aliases__\n").append("**").append(command.getKeyword()).append("**, **")
                        .append(FormatUtils.join(command.getAliases(), "**, **", "** or **"))
                        .append("**");
            }
            if (command.getChildren().length > 0) {
                builder.append("\n\n__Children__");
                for (Command child : command.getChildren()) {
                    builder.append("\n").append(chain).append(" ").append(command.getKeyword()).append(" **")
                            .append(child.getKeyword()).append("** - ").append(child.getDescription());
                }
            }
            event.replyDm(builder.toString());
        } else {
            doCommandList(event);
        }
    }

}
