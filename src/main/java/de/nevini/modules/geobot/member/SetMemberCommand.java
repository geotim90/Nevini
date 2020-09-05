package de.nevini.modules.geobot.member;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

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
