package de.nevini.modules.geobot.member;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.guild.activity.ActivityGetPlayingCommand;
import de.nevini.scope.Node;

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