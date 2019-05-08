package de.nevini.modules.core.prefix;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import de.nevini.resolvers.StringResolver;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class PrefixSetCommand extends Command {

    private final StringResolver resolver = new StringResolver("command prefix", "prefix");

    public PrefixSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .module(Module.CORE)
                .node(Node.CORE_PREFIX_SET)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("configures the command prefix")
                .syntax("<prefix>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        resolver.resolveArgumentOrOptionOrInput(event, (msg, prefix) -> acceptPrefix(event, msg, prefix));
    }

    private void acceptPrefix(CommandEvent event, Message message, String argument) {
        if (!argument.matches("\\S{1,32}")) {
            event.replyTo(message, CommandReaction.WARNING,
                    "The command prefix cannot be longer than 32 characters and must not contain spaces!");
        } else {
            event.getPrefixService().setGuildPrefix(event.getGuild(), argument);
            event.replyTo(message, CommandReaction.OK);
        }
    }

}
