package de.nevini.modules.legacy.report;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;
import org.springframework.stereotype.Component;

@Component
public class ReportCommand extends Command {

    public ReportCommand() {
        super(CommandDescriptor.builder()
                .keyword("report")
                .module(Module.LEGACY)
                .node(Node.LEGACY_REPORT)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("displays a legacy report")
                .syntax("[<user>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
