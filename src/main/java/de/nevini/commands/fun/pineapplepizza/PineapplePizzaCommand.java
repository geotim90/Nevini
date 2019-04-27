package de.nevini.commands.fun.pineapplepizza;

import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.bot.AbstractCommand;
import de.nevini.commands.fun.FunCategory;
import net.dv8tion.jda.core.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PineapplePizzaCommand extends AbstractCommand {

    public PineapplePizzaCommand(@Autowired FunCategory category) {
        super("pineapple-pizza", "which side are you on?", category);
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.aliases = new String[]{"pineapplepizza"};
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("https://www.youtube.com/watch?v=7t9z9KtoIJo");
    }

}
