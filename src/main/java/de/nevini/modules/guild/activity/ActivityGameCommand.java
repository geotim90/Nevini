package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;

public class ActivityGameCommand extends Command {

    protected ActivityGameCommand() {
        super(CommandDescriptor.builder()
                .keyword("--game")
                .module(Module.GUILD)
                .node(Node.GUILD_ACTIVITY_GAME)
                .description("displays game activity information")
                .syntax("<game>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
