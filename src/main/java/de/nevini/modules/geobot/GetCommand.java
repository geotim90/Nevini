package de.nevini.modules.geobot;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.geobot.contribution.GetContributionCommand;
import de.nevini.modules.geobot.member.GetMemberCommand;
import de.nevini.modules.geobot.role.GetRoleCommand;
import de.nevini.modules.geobot.timeout.GetGameCommand;
import de.nevini.modules.geobot.timeout.GetTimeoutCommand;

class GetCommand extends Command {

    GetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .children(new Command[]{
                        new GetContributionCommand(),
                        new GetGameCommand(),
                        new GetMemberCommand(),
                        new GetRoleCommand(),
                        new GetTimeoutCommand()
                })
                .description("Geobot style `get` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // do nothing
    }

}
