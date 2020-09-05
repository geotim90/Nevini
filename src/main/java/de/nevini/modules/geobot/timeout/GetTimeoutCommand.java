package de.nevini.modules.geobot.timeout;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

public class GetTimeoutCommand extends Command {

    public GetTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .children(new Command[]{
                        new GetTimeoutContributionCommand(),
                        new GetTimeoutLastMessageCommand(),
                        new GetTimeoutLastOnlineCommand()
                })
                .description("Geobot style `get timeout` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
