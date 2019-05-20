package de.nevini.modules.core.prefix;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;
import de.nevini.resolvers.StringResolver;

public class PrefixSetCommand extends Command {

    private final StringResolver resolver = new StringResolver("command prefix", "prefix");

    public PrefixSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .node(Node.CORE_PREFIX_SET)
                .description("configures the command prefix")
                .syntax("[--prefix] <prefix>")
                .details("__Options__\n**[--prefix] <prefix>** (required) - the prefix to use")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        resolver.resolveArgumentOrOptionOrInput(event, prefix -> acceptPrefix(event, prefix));
    }

    private void acceptPrefix(CommandEvent event, String argument) {
        if (!argument.matches("\\S{1,32}")) {
            event.reply(CommandReaction.WARNING,
                    "The command prefix cannot be longer than 32 characters and must not contain spaces!",
                    ignore -> event.complete());
        } else {
            event.getPrefixService().setGuildPrefix(event.getGuild(), argument);
            event.reply(CommandReaction.OK, ignore -> event.complete());
        }
    }

}
