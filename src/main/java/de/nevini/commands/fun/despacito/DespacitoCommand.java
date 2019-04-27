package de.nevini.commands.fun.despacito;

import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.bot.AbstractCommand;
import de.nevini.commands.fun.FunCategory;
import net.dv8tion.jda.core.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DespacitoCommand extends AbstractCommand {

    public DespacitoCommand(@Autowired FunCategory category) {
        super("despacito", "\uD83C\uDDF5\uD83C\uDDF7", category);
        this.guildOnly = false;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("https://www.youtube.com/watch?v=kJQP7kiw5Fk");
    }

}
