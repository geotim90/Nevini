package de.nevini.modules.geobot.member;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

public class SetMemberCommand extends Command {

    public SetMemberCommand() {
        super(CommandDescriptor.builder()
                .keyword("member")
                .children(new Command[]{
                        new SetMemberJoinedCommand(),
                        new SetMemberLastMessageCommand(),
                        new SetMemberLastOnlineCommand(),
                        new SetMemberLastPlayedCommand()
                })
                .description("Geobot style `set member` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
