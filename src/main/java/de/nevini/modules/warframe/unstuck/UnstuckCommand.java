package de.nevini.modules.warframe.unstuck;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
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
