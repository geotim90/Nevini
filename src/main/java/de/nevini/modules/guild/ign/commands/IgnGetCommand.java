package de.nevini.modules.guild.ign.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.modules.admin.game.data.GameData;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.TreeMap;

class IgnGetCommand extends Command {

    IgnGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.GUILD_IGN_GET)
                .description("displays in-game names for a user and/or game")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        Resolvers.GAME.describe()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrDefaultIfExists(event,
                event.getMember(),
                member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        Resolvers.GAME.resolveOptionOrInputIfExists(event, game -> acceptMemberGame(event, member, game));
    }

    private void acceptMemberGame(CommandEvent event, Member member, GameData game) {
        if (member == null && game == null) {
            displayMemberIgns(event, event.getMember());
        } else if (game == null) {
            displayMemberIgns(event, member);
        } else if (member == null) {
            displayGameIgns(event, game);
        } else {
            displayMemberGameIgn(event, member, game);
        }
    }

    private void displayMemberIgns(CommandEvent event, Member member) {
        Map<String, String> igns = new TreeMap<>();
        event.getIgnService().getIgns(member).forEach(data ->
                igns.put(event.getGameService().getGameName(data.getGame()), data.getName()));
        if (igns.isEmpty()) {
            event.reply("I have no in-game names for " + member.getEffectiveName() + ".", event::complete);
        } else {
            EmbedBuilder builder = event.createMemberEmbedBuilder(member);
            igns.forEach((game, ign) -> builder.addField(game, ign, true));
            event.reply(builder, event::complete);
        }
    }

    private void displayGameIgns(CommandEvent event, GameData game) {
        Map<String, String> igns = new TreeMap<>();
        event.getIgnService().getIgns(event.getGuild(), game).forEach(data -> {
            Member member = event.getGuild().getMemberById(data.getUser());
            if (member != null) igns.put(member.getEffectiveName(), data.getName());
        });
        if (igns.isEmpty()) {
            event.reply("I have no in-game names for " + game.getName() + ".", event::complete);
        } else {
            EmbedBuilder builder = event.createGameEmbedBuilder(game);
            igns.forEach((member, ign) -> builder.addField(member, ign, true));
            event.reply(builder, event::complete);
        }
    }

    private void displayMemberGameIgn(CommandEvent event, Member member, GameData game) {
        String ign = event.getIgnService().getInGameName(member, game);
        if (StringUtils.isEmpty(ign)) {
            event.reply("I do not have an in-game name for " + member.getEffectiveName() + " in "
                    + game.getName() + ".", event::complete);
        } else {
            event.reply(ign, event::complete);
        }
    }

}
