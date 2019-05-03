package de.nevini.modules.legacy.member;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class UnsetMemberCommand extends Command {

    public UnsetMemberCommand() {
        super(CommandDescriptor.builder()
                .keyword("member")
                .module(Module.LEGACY)
                .node(Node.LEGACY_UNSET_MEMBER)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("resets user activity information")
                .syntax("( (joined|lastOnline|lastMessage) <user> | lastPlayed <user> <game> )")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
