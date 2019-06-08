package de.nevini.bot.modules.core.guilds;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.scope.Node;
import de.nevini.bot.scope.Permissions;
import de.nevini.bot.util.Formatter;
import net.dv8tion.jda.core.entities.Guild;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GuildsCommand extends Command {

    protected GuildsCommand() {
        super(CommandDescriptor.builder()
                .keyword("guilds")
                .children(new Command[]{
                        new GuildsFeedCommand()
                })
                .ownerOnly(true)
                .guildOnly(false)
                .node(Node.CORE_HELP) // dummy node
                .minimumBotPermissions(Permissions.TALK)
                .description("display a list of all guilds this bot is in")
                .details("This command can only be executed by the owner of the bot.")
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
                    .append(") with ").append(Formatter.formatInteger(guild.getMembers().size())).append(" members");
        }
        event.reply(builder.toString(), event::complete);
    }

}
