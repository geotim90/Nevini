package de.nevini.modules.geobot.member;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.guild.activity.ActivitySetPlayingCommand;
import de.nevini.scope.Node;

class SetMemberLastPlayedCommand extends Command {

    private static final ActivitySetPlayingCommand delegate = new ActivitySetPlayingCommand();

    SetMemberLastPlayedCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastplayed")
                .aliases(new String[]{"last-played", "playing", "played"})
                .node(Node.GEOBOT_ADMIN)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **activity set playing**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
