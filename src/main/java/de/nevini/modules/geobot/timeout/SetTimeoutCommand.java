package de.nevini.modules.geobot.timeout;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

public class SetTimeoutCommand extends Command {

    public SetTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .children(new Command[]{
                        new SetTimeoutContributionCommand(),
                        new SetTimeoutLastMessageCommand(),
                        new SetTimeoutLastOnlineCommand()
                })
                .description("Geobot style `set timeout` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
