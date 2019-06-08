package de.nevini.bot.modules.core.prefix;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.StringResolver;
import de.nevini.bot.scope.Node;
import de.nevini.bot.scope.Permissions;
import de.nevini.framework.command.CommandOptionDescriptor;
import de.nevini.framework.command.CommandReaction;

public class PrefixSetCommand extends Command {

    private static final StringResolver prefixResolver = new StringResolver("command prefix", "prefix",
            CommandOptionDescriptor.builder()
                    .syntax("[--prefix] <prefix>")
                    .description("The command prefix to use. The flag is optional.")
                    .keyword("--prefix")
                    .aliases(new String[]{"//prefix"})
                    .build());

    public PrefixSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .node(Node.CORE_PREFIX_SET)
                .minimumBotPermissions(Permissions.sum(Permissions.TALK, Permissions.REACT))
                .description("configures the command prefix")
                .options(new CommandOptionDescriptor[]{
                        prefixResolver.describe()
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
