package de.nevini.modules.legacy.contribution;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class UnsetContributionCommand extends Command {

    public UnsetContributionCommand() {
        super(CommandDescriptor.builder()
                .keyword("contribution")
                .module(Module.LEGACY)
                .node(Node.LEGACY_UNSET_CONTRIBUTION)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("resets a user contribution")
                .syntax("<user>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
