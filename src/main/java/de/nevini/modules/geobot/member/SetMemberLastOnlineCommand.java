package de.nevini.modules.geobot.member;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.activity.commands.ActivitySetOnlineCommand;

class SetMemberLastOnlineCommand extends Command {

    private static final ActivitySetOnlineCommand delegate = new ActivitySetOnlineCommand();

    SetMemberLastOnlineCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastonline")
                .aliases(new String[]{"last-online", "online"})
                .node(Node.GEOBOT_ADMIN)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **activity set online**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
