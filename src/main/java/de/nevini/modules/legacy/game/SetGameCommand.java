package de.nevini.modules.legacy.game;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class SetGameCommand extends Command {

    public SetGameCommand() {
        super(CommandDescriptor.builder()
                .keyword("game")
                .module(Module.LEGACY)
                .node(Node.LEGACY_SET_GAME)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("configures the lastPlayed timeout for a game in days")
                .syntax("timeout <game> <days>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
