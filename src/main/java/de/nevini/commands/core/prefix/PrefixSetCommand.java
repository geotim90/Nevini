package de.nevini.commands.core.prefix;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.services.PrefixService;
import net.dv8tion.jda.core.Permission;

public class PrefixSetCommand extends Command {

    private final static int MAX_PREFIX_LENGTH = 24;

    private final PrefixService prefixService;

    public PrefixSetCommand(PrefixService prefixService) {
        this.prefixService = prefixService;
        this.name = "set";
        this.help = "configures the bot's prefix";
        this.arguments = "<prefix>";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
    }

    @Override
    protected void execute(CommandEvent event) {
        final String prefix = event.getArgs();
        if (prefix.isEmpty()) {
            event.reactError();
            event.reply("you did not provide a prefix to set...");
        } else if (prefix.length() > MAX_PREFIX_LENGTH) {
            event.reactError();
            event.reply("The prefix you provided is too long! " +
                    "Please choose one that no more than " + MAX_PREFIX_LENGTH + " characters long.");
        } else {
            prefixService.setGuildPrefix(event.getGuild(), prefix);
            event.reactSuccess();
        }
    }

}