package de.nevini.modules.geobot;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.geobot.contribution.SetContributionCommand;
import de.nevini.modules.geobot.member.SetMemberCommand;
import de.nevini.modules.geobot.timeout.SetGameCommand;
import de.nevini.modules.geobot.timeout.SetTimeoutCommand;

class SetCommand extends Command {

    SetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .children(new Command[]{
                        new SetContributionCommand(),
                        new SetGameCommand(),
                        new SetMemberCommand(),
                        new SetTimeoutCommand()
                })
                .description("Geobot style `set` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // do nothing
    }

}
