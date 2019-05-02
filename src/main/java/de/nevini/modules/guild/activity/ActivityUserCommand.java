package de.nevini.modules.guild.activity;

import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandWithRequiredArgument;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.entities.Message;

public class ActivityUserCommand extends CommandWithRequiredArgument {

    protected ActivityUserCommand() {
        super(CommandDescriptor.builder()
                .keyword("--user")
                .aliases(new String[]{"-u"})
                .module(Module.GUILD)
                .node(Node.GUILD_ACTIVITY_USER)
                .description("displays user activity information")
                .syntax("<user>")
                        .build(),
                "a user");
    }

    @Override
    protected void acceptArgument(CommandEvent event, Message message, String argument) {
        // TODO
    }

}
