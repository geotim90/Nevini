package de.nevini.modules.geobot.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

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
