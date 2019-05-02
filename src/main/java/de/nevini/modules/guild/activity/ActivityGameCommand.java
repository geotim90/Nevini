package de.nevini.modules.guild.activity;

import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandWithRequiredArgument;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.entities.Message;

public class ActivityGameCommand extends CommandWithRequiredArgument {

    protected ActivityGameCommand() {
        super(CommandDescriptor.builder()
                .keyword("--game")
                .module(Module.GUILD)
                .node(Node.GUILD_ACTIVITY_GAME)
                .description("displays game activity information")
                .syntax("<game>")
                        .build(),
                "a game");
    }

    @Override
    protected void acceptArgument(CommandEvent event, Message message, String argument) {
        // TODO
    }

}
