package de.nevini.modules.core.guilds;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.util.Formatter;
import net.dv8tion.jda.core.entities.Guild;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GuildsCommand extends Command {

    protected GuildsCommand() {
        super(CommandDescriptor.builder()
                .keyword("guilds")
                .children(new Command[]{
                        new GuildsFeedCommand(),
                        new GuildsLeaveCommand(),
                })
                .ownerOnly(true)
                .guildOnly(false)
                .node(Node.CORE_HELP) // dummy node
                .minimumBotPermissions(Permissions.TALK)
                .description("display a list of all guilds this bot is in")
                .build());
    }

    @Override
    public void execute(CommandEvent event) {
        List<Guild> guilds = event.getJDA().getGuilds();
        StringBuilder builder = new StringBuilder();
        builder.append("**Guilds on this shard**: ").append(guilds.size()).append('\n');
        for (Guild guild : guilds) {
            builder.append("\n**").append(guild.getName()).append("** (").append(guild.getId()).append(") owned by **")
                    .append(guild.getOwner().getUser().getAsTag()).append("** (").append(guild.getOwnerId())
                    .append(") with ").append(Formatter.formatLong(count(guild, false))).append(" members and ")
                    .append(Formatter.formatLong(count(guild, true))).append(" bots");
        }
        event.reply(builder.toString(), event::complete);
    }

    private long count(Guild guild, boolean bots) {
        return guild.getMembers().stream().filter(member -> member.getUser().isBot() == bots).count();
    }

}
