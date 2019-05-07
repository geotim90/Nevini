package de.nevini.modules.legacy;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import net.dv8tion.jda.core.Permission;
import org.springframework.stereotype.Component;

@Component
public class RemoveCommand extends Command {

    public RemoveCommand() {
        super(CommandDescriptor.builder()
                .keyword("remove")
                .children(new Command[]{
                        // TODO
                })
                .module(Module.LEGACY)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("executes legacy `remove` commands")
                .syntax("") // TODO
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
