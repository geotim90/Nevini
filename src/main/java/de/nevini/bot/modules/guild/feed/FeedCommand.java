package de.nevini.bot.modules.guild.feed;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class FeedCommand extends Command {

    public FeedCommand() {
        super(CommandDescriptor.builder()
                .keyword("feed")
                .aliases(new String[]{"feeds", "subscription", "subscriptions"})
                .children(new Command[]{
                        new FeedGetCommand(),
                        new FeedSetCommand()
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
