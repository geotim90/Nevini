package de.nevini.modules.guild.tribute.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;

public class TributeTimeoutSetCommand extends Command {

    public TributeTimeoutSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .node(Node.GUILD_TRIBUTE_TIMEOUT_SET)
                .description("configures the timeout for users that need to contribute")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.DURATION.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.DURATION.resolveArgumentOrOptionOrInput(event, duration -> acceptDuration(event, duration));
    }

    private void acceptDuration(CommandEvent event, Long duration) {
        event.getTributeService().setTimeout(event.getGuild(), duration);
        event.reply(CommandReaction.OK, event::complete);
    }

}
