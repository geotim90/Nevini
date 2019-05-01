package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;

public class ActivityUserCommand extends Command {

    protected ActivityUserCommand() {
        super(CommandDescriptor.builder()
                .keyword("--user")
                .aliases(new String[]{"-u"})
                .module(Module.GUILD)
                .node(Node.GUILD_ACTIVITY_USER)
                .description("displays user activity information")
                .syntax("<user>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
