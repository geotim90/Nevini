package de.nevini.modules.geobot.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

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
