package de.nevini.commands.fun.thefox;

import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.bot.AbstractCommand;
import de.nevini.commands.fun.FunCategory;
import net.dv8tion.jda.core.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TheFoxCommand extends AbstractCommand {

    public TheFoxCommand(@Autowired FunCategory category) {
        super("the-fox", "what does the fox say?", category);
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.aliases = new String[]{"thefox", "what-does-the-fox-say", "whatdoesthefoxsay", "what-the-fox-say", "whatthefoxsay"};
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("https://www.youtube.com/watch?v=jofNR_WkoCE");
    }

}
