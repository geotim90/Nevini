package de.nevini.modules.geobot.member;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.guild.tribute.TributeStartGetCommand;
import de.nevini.scope.Node;

class GetMemberJoinedCommand extends Command {

    private static final TributeStartGetCommand delegate = new TributeStartGetCommand();

    GetMemberJoinedCommand() {
        super(CommandDescriptor.builder()
                .keyword("joined")
                .aliases(new String[]{"start", "tracked"})
                .node(Node.GEOBOT_MOD)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **tribute start get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
