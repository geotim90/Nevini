package de.nevini.commands.guild;

import com.jagrosh.jdautilities.command.Command;
import org.springframework.stereotype.Component;

@Component
public class GuildCategory extends Command.Category {

    public GuildCategory() {
        super("guild");
    }

}
