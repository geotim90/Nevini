package de.nevini.modules.warframe.commands.unstuck;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import org.springframework.stereotype.Component;

@Component
public class UnstuckCommand extends Command {

    public UnstuckCommand() {
        super(CommandDescriptor.builder()
                .keyword("unstuck")
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("will attempt to teleport the player to their last good position")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("You have been unstuck!", event::complete);
    }

}
