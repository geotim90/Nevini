package de.nevini.modules.legacy;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import net.dv8tion.jda.core.Permission;

public class UnsetCommand extends Command {

    public UnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .children(new Command[]{
                        // TODO
                })
                .module(Module.LEGACY)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("executes legacy `unset` commands")
                .syntax("") // TODO
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
