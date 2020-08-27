package de.nevini.modules.admin.guilds.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Guild;

class GuildsLeaveCommand extends Command {

    GuildsLeaveCommand() {
        super(CommandDescriptor.builder()
                .keyword("leave")
                .ownerOnly(true)
                .guildOnly(false)
                .node(Node.CORE_HELP) // dummy node
                .description("makes the bot leave a guild")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.GUILD.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.GUILD.resolveArgumentOrOptionOrInput(event, guild -> acceptGuild(event, guild));
    }

    private void acceptGuild(CommandEvent event, Guild guild) {
        guild.leave().queue(
                ignore -> event.reply(CommandReaction.OK, event::complete),
                ignore -> event.reply(CommandReaction.ERROR, event::complete)
        );
    }

}
