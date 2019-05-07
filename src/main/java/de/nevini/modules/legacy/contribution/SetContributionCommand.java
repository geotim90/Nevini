package de.nevini.modules.legacy.contribution;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class SetContributionCommand extends Command {

    public SetContributionCommand() {
        super(CommandDescriptor.builder()
                .keyword("contribution")
                .module(Module.LEGACY)
                .node(Node.LEGACY_SET_CONTRIBUTION)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("confirms that a user has made a contribution")
                .syntax("<user>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
