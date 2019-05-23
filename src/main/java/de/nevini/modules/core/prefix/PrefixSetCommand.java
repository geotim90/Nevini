package de.nevini.modules.core.prefix;

import de.nevini.command.*;
import de.nevini.modules.Node;
import de.nevini.resolvers.StringResolver;

public class PrefixSetCommand extends Command {

    private static final StringResolver prefixResolver = new StringResolver("command prefix", "prefix");

    public PrefixSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .node(Node.CORE_PREFIX_SET)
                .description("configures the command prefix")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("[--prefix] <prefix>")
                                .description("The command prefix to use. The flag is optional.")
                                .keyword("--prefix")
                                .aliases(new String[]{"//prefix"})
                                .build()
                })
                .details("The command prefix cannot be longer than 32 characters and must not contain spaces.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        prefixResolver.resolveArgumentOrOptionOrInput(event, prefix -> acceptPrefix(event, prefix));
    }

    private void acceptPrefix(CommandEvent event, String argument) {
        if (!argument.matches("\\S{1,32}")) {
            event.reply(CommandReaction.WARNING,
                    "The command prefix cannot be longer than 32 characters and must not contain spaces!",
                    event::complete);
        } else {
            event.getPrefixService().setGuildPrefix(event.getGuild(), argument);
            event.reply(CommandReaction.OK, event::complete);
        }
    }

}
