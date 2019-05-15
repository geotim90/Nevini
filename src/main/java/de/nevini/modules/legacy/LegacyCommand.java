package de.nevini.modules.legacy;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.legacy.report.ReportCommand;
import org.springframework.stereotype.Component;

@Component
public class LegacyCommand extends Command {

    public LegacyCommand() {
        super(CommandDescriptor.builder()
                .keyword("legacy")
                .aliases(new String[]{"geobot"})
                .children(new Command[]{
                        new ReportCommand(),
                        new AddCommand(),
                        new GetCommand(),
                        new RemoveCommand(),
                        new SetCommand(),
                        new UnsetCommand()
                })
                .description("executes legacy Geobot commands")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // delegate to child despite missing "report" keyword
        getChildren()[0].onEvent(event);
    }

}
