package de.nevini.commands.core.ping;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.bot.CommandComponent;
import de.nevini.commands.core.CoreCategory;
import net.dv8tion.jda.core.Permission;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;

/**
 * @see com.jagrosh.jdautilities.examples.command.PingCommand
 */
@CommandComponent
public class PingCommand extends Command {

    public PingCommand(@Autowired CoreCategory coreCategory) {
        this.name = "ping";
        this.help = "checks the bot's latency";
        this.category = coreCategory;
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Ping: ...", m -> {
            long ping = event.getMessage().getCreationTime().until(m.getCreationTime(), ChronoUnit.MILLIS);
            m.editMessage("Ping: " + ping + "ms | Websocket: " + event.getJDA().getPing() + "ms").queue();
        });
    }

}
