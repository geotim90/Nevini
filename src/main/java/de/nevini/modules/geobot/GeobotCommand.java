package de.nevini.modules.geobot;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.core.help.HelpCommand;
import de.nevini.modules.core.ping.PingCommand;
import de.nevini.modules.geobot.report.ReportCommand;
import org.springframework.stereotype.Component;

@Component
public class GeobotCommand extends Command {

    private static final ReportCommand reportCommand = new ReportCommand();

    public GeobotCommand() {
        super(CommandDescriptor.builder()
                .keyword("geobot")
                .aliases(new String[]{"geo"})
                .children(new Command[]{
                        reportCommand,
                        new PingCommand(),
                        new HelpCommand(),
                        new GetCommand(),
                        new SetCommand(),
                        new UnsetCommand(),
                        new AddCommand(),
                        new RemoveCommand()
                })
                .description("Geobot style command emulation")
                .details("By default, this command will behave the same as **geobot report** for the current user.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        reportCommand.doMemberReport(event, event.getMember());
    }

}
