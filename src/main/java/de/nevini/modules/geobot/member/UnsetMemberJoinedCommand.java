package de.nevini.modules.geobot.member;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.guild.tribute.TributeStartUnsetCommand;
import de.nevini.scope.Node;

class UnsetMemberJoinedCommand extends Command {

    private static final TributeStartUnsetCommand delegate = new TributeStartUnsetCommand();

    UnsetMemberJoinedCommand() {
        super(CommandDescriptor.builder()
                .keyword("joined")
                .aliases(new String[]{"start", "tracked"})
                .node(Node.GEOBOT_ADMIN)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **tribute start unset**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
