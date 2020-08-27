package de.nevini.modules.geobot.timeout;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

public class UnsetTimeoutCommand extends Command {

    public UnsetTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .children(new Command[]{
                        new UnsetTimeoutContributionCommand(),
                        new UnsetTimeoutLastMessageCommand(),
                        new UnsetTimeoutLastOnlineCommand()
                })
                .description("Geobot style `unset timeout` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
