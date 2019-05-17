package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;

public class SetTimeoutCommand extends Command {

    public SetTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .children(new Command[]{
                        new SetTimeoutContributionCommand(),
                        new SetTimeoutLastOnlineCommand(),
                        new SetTimeoutLastMessageCommand()
                })
                .node(Node.LEGACY_SET_TIMEOUT)
                .description("configures timeouts in days")
                .syntax("(contribution|lastOnline|lastMessage) <days>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(CommandReaction.WARNING, "You did not specify one of `contribution`, `lastOnline` or `lastMessage`!", event::complete);
    }

}
