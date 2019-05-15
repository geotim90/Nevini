package de.nevini.modules.legacy;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.legacy.contribution.UnsetContributionCommand;
import de.nevini.modules.legacy.game.UnsetGameCommand;
import de.nevini.modules.legacy.member.UnsetMemberCommand;
import de.nevini.modules.legacy.timeout.UnsetTimeoutCommand;
import org.springframework.stereotype.Component;

@Component
public class UnsetCommand extends Command {

    public UnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .children(new Command[]{
                        new UnsetContributionCommand(),
                        new UnsetGameCommand(),
                        new UnsetMemberCommand(),
                        new UnsetTimeoutCommand()
                })
                .description("executes legacy `unset` commands")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // do nothing if no child keyword was found
    }

}
