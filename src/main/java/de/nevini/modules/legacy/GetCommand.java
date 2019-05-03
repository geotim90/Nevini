package de.nevini.modules.legacy;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.legacy.contribution.GetContributionCommand;
import de.nevini.modules.legacy.game.GetGameCommand;
import de.nevini.modules.legacy.member.GetMemberCommand;
import de.nevini.modules.legacy.role.GetRoleCommand;
import de.nevini.modules.legacy.timeout.GetTimeoutCommand;
import net.dv8tion.jda.core.Permission;
import org.springframework.stereotype.Component;

@Component
public class GetCommand extends Command {

    public GetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .children(new Command[]{
                        new GetContributionCommand(),
                        new GetGameCommand(),
                        new GetMemberCommand(),
                        new GetRoleCommand(),
                        new GetTimeoutCommand()
                })
                .module(Module.LEGACY)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("executes legacy `get` commands")
                .syntax("( contribution <user> | game timeout <game> | member ( (joined|lastOnline|lastMessage) <user> " +
                        "| lastPlayed <user> <game> ) | role (initiate|member|mod|admin) | timeout (joined|lastOnline|" +
                        "lastMessage) )")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
