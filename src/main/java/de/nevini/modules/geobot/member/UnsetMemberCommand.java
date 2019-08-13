package de.nevini.modules.geobot.member;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

public class UnsetMemberCommand extends Command {

    public UnsetMemberCommand() {
        super(CommandDescriptor.builder()
                .keyword("member")
                .children(new Command[]{
                        new UnsetMemberJoinedCommand(),
                        new UnsetMemberLastMessageCommand(),
                        new UnsetMemberLastOnlineCommand(),
                        new UnsetMemberLastPlayedCommand()
                })
                .description("Geobot style `unset member` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
