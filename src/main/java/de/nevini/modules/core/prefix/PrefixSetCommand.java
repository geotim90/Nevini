package de.nevini.modules.core.prefix;

import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.command.CommandWithRequiredArgument;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.entities.Message;

public class PrefixSetCommand extends CommandWithRequiredArgument {

    public PrefixSetCommand() {
        super(CommandDescriptor.builder()
                        .keyword("set")
                        .module(Module.CORE)
                        .node(Node.CORE_PREFIX_SET)
                        .description("configures the command prefix")
                        .syntax("<prefix>")
                        .build(),
                "a command prefix");
    }

    @Override
    protected void acceptArgument(CommandEvent event, Message message, String argument) {
        if (!argument.matches("\\S{1,32}")) {
            event.replyTo(message, CommandReaction.WARNING,
                    "The command prefix cannot be longer than 32 characters and must not contain spaces!");
        } else {
            event.getPrefixService().setGuildPrefix(event.getGuild(), argument);
            event.replyTo(message, CommandReaction.OK);
        }
    }

}
