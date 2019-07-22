package de.nevini.modules.guild.feed;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class FeedCommand extends Command {

    public FeedCommand() {
        super(CommandDescriptor.builder()
                .keyword("feed")
                .aliases(new String[]{"feeds", "subscription", "subscriptions"})
                .children(new Command[]{
                        new FeedGetCommand(),
                        new FeedSetCommand(),
                        new FeedUnsetCommand()
                })
                .description("displays and configures feeds")
                .details("By default, this command will behave the same as **feed get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
