package de.nevini.modules.geobot.member;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.activity.commands.ActivitySetPlayingCommand;

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
