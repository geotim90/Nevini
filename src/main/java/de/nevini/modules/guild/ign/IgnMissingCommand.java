package de.nevini.modules.guild.ign;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.jpa.game.GameData;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.LinkedHashSet;
import java.util.Set;

class IgnMissingCommand extends Command {

    IgnMissingCommand() {
        super(CommandDescriptor.builder()
                .keyword("missing")
                .node(Node.GUILD_IGN_GET)
                .description("finds users missing in-game names for a game")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.GAME.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.GAME.resolveArgumentOrOptionOrInput(event, game -> acceptGame(event, game));
    }

    private void acceptGame(CommandEvent event, GameData game) {
        Set<Member> members = new LinkedHashSet<>(event.getGuild().getMembers());
        members.removeIf(member -> member.getUser().isBot());
        event.getIgnService().getIgns(event.getGuild(), game).forEach(ign -> {
            Member member = event.getGuild().getMemberById(ign.getUser());
            if (member != null) members.remove(member);
        });
        if (members.isEmpty()) {
            event.reply("No users missing in-game names for " + game.getName() + ".", event::complete);
        } else {
            EmbedBuilder builder = event.createEmbedBuilder();
            builder.setAuthor(game.getName(), null, game.getIcon());
            members.forEach(member -> builder.addField(member.getEffectiveName(),
                    member.getUser().getId() + '\n' + member.getUser().getAsTag(), true));
            event.reply(builder, event::complete);
        }
    }

}
