package de.nevini.modules.geobot.member;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

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
