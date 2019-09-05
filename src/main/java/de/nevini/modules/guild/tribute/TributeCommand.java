package de.nevini.modules.guild.tribute;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class TributeCommand extends Command {

    public TributeCommand() {
        super(CommandDescriptor.builder()
                .keyword("tribute")
                .aliases(new String[]{"contribution", "donation"})
                .children(new Command[]{
                        new TributeGetCommand(),
                        new TributeSetCommand(),
                        new TributeUnsetCommand(),
                        new TributeRoleCommand(),
                        new TributeTimeoutCommand(),
                        new TributeStartCommand(),
                        new TributeDelayCommand(),
                        new TributeReportCommand()
                })
                .description("displays and configures user contributions")
                .details("By default, this command will behave the same as **tribute get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
