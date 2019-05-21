package de.nevini.modules.guild.ign;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.db.game.GameData;
import de.nevini.db.ign.IgnData;
import de.nevini.modules.Node;
import de.nevini.resolvers.GameResolver;
import de.nevini.resolvers.MemberResolver;
import de.nevini.resolvers.Resolvers;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class IgnGetCommand extends Command {

    public IgnGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.GUILD_IGN_GET)
                .description("displays in-game names for a user and/or game")
                .options(new CommandOptionDescriptor[]{
                        MemberResolver.describe().build(),
                        GameResolver.describe().build()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrDefaultIfExists(event, event.getMember(), member -> acceptMember(event, member));
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
        List<IgnData> igns = event.getIgnService().getIgns(member);
        if (igns.isEmpty()) {
            event.reply("I have no in-game names for " + member.getEffectiveName() + ".", event::complete);
        } else {
            EmbedBuilder builder = event.createEmbedBuilder();
            builder.setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl());
            for (IgnData ign : igns) {
                String gameName = event.getGameService().getGameName(ign.getGame());
                builder.addField(gameName, ign.getName(), true);
            }
            event.reply(builder, event::complete);
        }
    }

    private void displayGameIgns(CommandEvent event, GameData game) {
        List<IgnData> igns = event.getIgnService().getIgns(event.getGuild(), game);
        if (igns.isEmpty()) {
            event.reply("I have no in-game names for " + game.getName() + ".", event::complete);
        } else {
            EmbedBuilder builder = event.createEmbedBuilder();
            builder.setAuthor(game.getName(), null, game.getIcon());
            for (IgnData ign : igns) {
                Member member = event.getGuild().getMemberById(ign.getUser());
                if (member != null) builder.addField(member.getEffectiveName(), ign.getName(), true);
            }
            event.reply(builder, event::complete);
        }
    }

    private void displayMemberGameIgn(CommandEvent event, Member member, GameData game) {
        String ign = event.getIgnService().getIgn(member, game);
        if (StringUtils.isEmpty(ign)) {
            event.reply("I do not have an in-game name for " + member.getEffectiveName() + " in " + game.getName() + ".", event::complete);
        } else {
            event.reply(ign, event::complete);
        }
    }

}
