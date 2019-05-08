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
                        new AddCommand(),
                        new GetCommand(),
                        new RemoveCommand(),
                        new SetCommand(),
                        new UnsetCommand(),
                        new ReportCommand()
                })
                .description("executes legacy Geobot commands")
                .syntax("( (add|get|remove|set|unset) ... | report [<user>] )")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
