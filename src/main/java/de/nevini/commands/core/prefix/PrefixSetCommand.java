package de.nevini.commands.core.prefix;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.services.PrefixService;
import net.dv8tion.jda.core.Permission;

public class PrefixSetCommand extends Command {

    private final static int MAX_PREFIX_LENGTH = 24;

    private final PrefixService prefixService;

    public PrefixSetCommand(PrefixService prefixService) {
        this.name = "set";
        this.help = "configures the bot's prefix";
        this.arguments = "<prefix>";
        this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION};
        this.prefixService = prefixService;
    }

    @Override
    protected void execute(CommandEvent event) {
        String prefix = event.getArgs();
        if (prefix.isEmpty()) {
            event.replyWarning(event.getAuthor().getAsMention() + ", you did not provide a prefix to set...");
        } else if (prefix.length() > MAX_PREFIX_LENGTH) {
            event.replyError(event.getAuthor().getAsMention() + ", the prefix you provided is too long! " +
                    "Please choose one that no more than " + MAX_PREFIX_LENGTH + " characters long.");
        } else {
            prefixService.setGuildPrefix(event.getGuild(), prefix);
            event.reactSuccess();
        }
    }

}
