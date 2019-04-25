package de.nevini.commands.core;

import com.jagrosh.jdautilities.command.Command;
import org.springframework.stereotype.Component;

@Component
public class CoreCategory extends Command.Category {

    public CoreCategory() {
        super("core");
    }

}
