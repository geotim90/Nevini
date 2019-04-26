package de.nevini.commands.core.prefix;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.services.PrefixService;
import net.dv8tion.jda.core.Permission;

public class PrefixGetCommand extends Command {

    private final PrefixService prefixService;

    public PrefixGetCommand(PrefixService prefixService) {
        this.name = "get";
        this.help = "displays the bot's prefix";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.prefixService = prefixService;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(prefixService.getGuildPrefix(event.getGuild()));
    }

}
