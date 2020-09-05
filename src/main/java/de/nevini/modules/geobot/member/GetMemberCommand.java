package de.nevini.modules.geobot.member;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

public class GetMemberCommand extends Command {

    public GetMemberCommand() {
        super(CommandDescriptor.builder()
                .keyword("member")
                .children(new Command[]{
                        new GetMemberJoinedCommand(),
                        new GetMemberLastOnlineCommand(),
                        new GetMemberLastMessageCommand(),
                        new GetMemberLastPlayedCommand()
                })
                .description("Geobot style `get member` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
