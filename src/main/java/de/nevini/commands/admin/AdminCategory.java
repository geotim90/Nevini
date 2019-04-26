package de.nevini.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import org.springframework.stereotype.Component;

@Component
public class AdminCategory extends Command.Category {

    public AdminCategory() {
        super("admin");
    }

}
