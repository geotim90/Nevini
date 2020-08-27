package de.nevini.modules.geobot;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.modules.geobot.contribution.UnsetContributionCommand;
import de.nevini.modules.geobot.member.UnsetMemberCommand;
import de.nevini.modules.geobot.timeout.UnsetGameCommand;
import de.nevini.modules.geobot.timeout.UnsetTimeoutCommand;

class UnsetCommand extends Command {

    UnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .children(new Command[]{
                        new UnsetContributionCommand(),
                        new UnsetGameCommand(),
                        new UnsetMemberCommand(),
                        new UnsetTimeoutCommand()
                })
                .description("Geobot style `unset` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // do nothing
    }

}
