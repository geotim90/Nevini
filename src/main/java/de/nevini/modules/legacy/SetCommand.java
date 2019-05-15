package de.nevini.modules.legacy;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.legacy.contribution.SetContributionCommand;
import de.nevini.modules.legacy.game.SetGameCommand;
import de.nevini.modules.legacy.member.SetMemberCommand;
import de.nevini.modules.legacy.timeout.SetTimeoutCommand;
import org.springframework.stereotype.Component;

@Component
public class SetCommand extends Command {

    public SetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .children(new Command[]{
                        new SetContributionCommand(),
                        new SetGameCommand(),
                        new SetMemberCommand(),
                        new SetTimeoutCommand()
                })
                .description("executes legacy `set` commands")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // do nothing if no child keyword was found
    }

}