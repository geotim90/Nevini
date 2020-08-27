package de.nevini.modules.geobot.member;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.tribute.commands.TributeStartGetCommand;

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
