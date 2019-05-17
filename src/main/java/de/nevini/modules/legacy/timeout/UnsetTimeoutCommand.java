package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;

public class UnsetTimeoutCommand extends Command {

    public UnsetTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .children(new Command[]{
                        new UnsetTimeoutContributionCommand(),
                        new UnsetTimeoutLastOnlineCommand(),
                        new UnsetTimeoutLastMessageCommand()
                })
                .node(Node.LEGACY_UNSET_TIMEOUT)
                .description("removes timeouts")
                .syntax("(contribution|lastOnline|lastMessage)")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(CommandReaction.WARNING, "You did not specify one of `contribution`, `lastOnline` or `lastMessage`!", event::complete);
    }

}
