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
import net.dv8tion.jda.api.entities.Role;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

class IgnMissingCommand extends Command {

    IgnMissingCommand() {
        super(CommandDescriptor.builder()
                .keyword("missing")
                .node(Node.GUILD_IGN_GET)
                .description("finds users missing in-game names for a game")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.GAME.describe(false, true),
                        Resolvers.ROLE.describe(false, false)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        if (Resolvers.ROLE.isOptionPresent(event)) {
            Resolvers.ROLE.resolveOptionOrInput(event, role -> acceptRole(event, role));
        } else {
            acceptRole(event, null);
        }
    }

    private void acceptRole(CommandEvent event, Role role) {
        Resolvers.GAME.resolveArgumentOrOptionOrInput(event, game -> acceptRoleGame(event, role, game));
    }

    private void acceptRoleGame(CommandEvent event, Role role, GameData game) {
        Set<Member> members = new HashSet<>(role == null ? event.getGuild().getMembers()
                : event.getGuild().getMembersWithRoles(role));
        members.removeIf(member -> member.getUser().isBot());
        event.getIgnService().getIgns(event.getGuild(), game).forEach(ign -> {
            Member member = event.getGuild().getMemberById(ign.getUser());
            if (member != null) members.remove(member);
        });
        if (members.isEmpty()) {
            event.reply("No users missing in-game names for " + game.getName() + ".", event::complete);
        } else {
            EmbedBuilder builder = event.createGameEmbedBuilder(game);
            members.stream().sorted(Comparator.comparing(Member::getEffectiveName))
                    .forEach(member -> builder.addField(member.getEffectiveName(),
                            member.getUser().getId() + '\n' + member.getUser().getAsTag(), true));
            event.reply(builder, event::complete);
        }
    }

}
