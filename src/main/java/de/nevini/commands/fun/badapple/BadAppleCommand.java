package de.nevini.commands.fun.badapple;

import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.bot.AbstractCommand;
import de.nevini.commands.fun.FunCategory;
import net.dv8tion.jda.core.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BadAppleCommand extends AbstractCommand {

    public BadAppleCommand(@Autowired FunCategory category) {
        super("bad-apple", "BAD APPLE!! || METAL COVER by RichaadEB ft. Cristina Vee", category);
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.aliases = new String[]{"badapple"};
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("https://www.youtube.com/watch?v=9Xz4NV0zsbY");
    }

}
