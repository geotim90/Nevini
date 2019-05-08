package de.nevini.modules.legacy.report;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;
import org.springframework.stereotype.Component;

@Component
public class ReportCommand extends Command {

    public ReportCommand() {
        super(CommandDescriptor.builder()
                .keyword("report")
                .node(Node.LEGACY_REPORT)
                .description("displays a legacy report")
                .syntax("[<user>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
