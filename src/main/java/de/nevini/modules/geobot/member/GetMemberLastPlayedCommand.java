package de.nevini.modules.geobot.member;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.activity.commands.ActivityGetPlayingCommand;

class GetMemberLastPlayedCommand extends Command {

    private static final ActivityGetPlayingCommand delegate = new ActivityGetPlayingCommand();

    GetMemberLastPlayedCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastplayed")
                .aliases(new String[]{"last-played", "playing", "played"})
                .node(Node.GEOBOT_MOD)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **activity get playing**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
