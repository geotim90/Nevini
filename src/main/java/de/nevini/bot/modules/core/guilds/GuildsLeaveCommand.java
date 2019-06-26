package de.nevini.bot.modules.core.guilds;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandOptionDescriptor;
import de.nevini.framework.command.CommandReaction;
import net.dv8tion.jda.core.entities.Guild;

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
