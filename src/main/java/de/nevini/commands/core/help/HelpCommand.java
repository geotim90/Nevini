package de.nevini.commands.core.help;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.bot.AbstractCommand;
import de.nevini.bot.CommandComponent;
import de.nevini.commands.core.CoreCategory;
import de.nevini.services.PrefixService;
import net.dv8tion.jda.core.entities.ChannelType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;

@CommandComponent
public class HelpCommand extends AbstractCommand {

    private final PrefixService prefixService;

    public HelpCommand(
            @Autowired CoreCategory category,
            @Autowired PrefixService prefixService
    ) {
        super("help", "gets help", category);
        this.arguments = "[command]";
        this.guildOnly = false;
        this.prefixService = prefixService;
    }

    @Override
    protected void execute(CommandEvent event) {
        String args = event.getArgs();
        if (args.isEmpty()) {
            doHelp(event);
        } else {
            drilldown(event, prefixService.getGuildPrefix(event.getGuild()), splitArgs(args), event.getClient().getCommands());
        }
    }

    private void doHelp(CommandEvent event) {
        StringBuilder builder = new StringBuilder("Here is a list of all **" + event.getSelfUser().getName() + "** commands.");
        String prefix = prefixService.getGuildPrefix(event.getGuild());
        Category category = null;
        for (Command command : event.getClient().getCommands()) {
            if (!command.isHidden() && (!command.isOwnerCommand() || event.isOwner())) {
                if (!command.getCategory().equals(category)) {
                    category = command.getCategory();
                    builder.append("\n\n__Module: **").append(category.getName()).append("**__");
                }
                builder.append("\n").append(prefix).append(" **").append(command.getName()).append("** - ").append(command.getHelp());
            }
        }
        builder.append("\n\nFor additional help, join ").append(event.getClient().getServerInvite());
        replyInDm(event, builder.toString());
    }

    private void drilldown(CommandEvent event, String prefix, String[] args, Collection<Command> commands) {
        Command command = commands.stream().filter(cmd -> cmd.isCommandFor(args[0])).findAny().orElse(null);
        if (command != null && command.getChildren().length > 0 && args.length > 1 && !args[1].isEmpty()) {
            drilldown(event, prefix + " " + command.getName(), splitArgs(args[1]), Arrays.asList(command.getChildren()));
        } else {
            doCommandHelp(event, prefix, command);
        }
    }

    private String[] splitArgs(String args) {
        return args.split("\\s+", 2);
    }

    private void doCommandHelp(CommandEvent event, String prefix, Command command) {
        if (command != null && !command.isHidden() && (!command.isOwnerCommand() || event.isOwner())) {
            StringBuilder builder = new StringBuilder("__Module: **" + category.getName() + "**__\n" + prefix + " **" + command.getName() + "** - " + command.getHelp());
            if (!StringUtils.isEmpty(command.getArguments())) {
                builder.append("\n\n__Usage__\n`").append(prefix).append(" ").append(command.getName()).append(" ").append(command.getArguments()).append("`");
            }
            if (command.getAliases().length > 0) {
                builder.append("\n\n__Aliases__\n`").append("for **").append(command.getName()).append("** - **")
                        .append(StringUtils.join(command.getAliases(), "** | **")).append("**");
            }
            if (command.getChildren().length > 0) {
                builder.append("\n\n__Children__");
                for (Command child : command.getChildren()) {
                    builder.append("\n").append(prefix).append(" ").append(command.getName()).append(" **").append(child.getName()).append("** - ").append(child.getHelp());
                }
            }
            replyInDm(event, builder.toString());
        } else {
            doHelp(event);
        }
    }

    private void replyInDm(CommandEvent event, String content) {
        event.replyInDm(content, message -> {
            if (event.isFromType(ChannelType.TEXT)) event.reactSuccess();
        }, t -> event.replyWarning(event.getAuthor().getAsMention() + ", you are blocking direct messages."));
    }

}
