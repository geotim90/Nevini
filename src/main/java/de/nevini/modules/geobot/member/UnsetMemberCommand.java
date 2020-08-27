package de.nevini.modules.geobot.member;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

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
