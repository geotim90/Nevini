package de.nevini.modules.util.unicode.obfuscate;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ObfuscateCommand extends Command {

    public ObfuscateCommand() {
        super(CommandDescriptor.builder()
                .keyword("obfuscate")
                .guildOnly(false)
                .node(Node.UTIL_UNICODE_OBFUSCATE)
                .minimumBotPermissions(Permissions.TALK)
                .description("obfuscates text")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        String arg = event.getArgument();
        if (StringUtils.isNotBlank(arg)) {
            event.reply(Obfuscator.obfuscate(arg), event::complete);
        }
    }

}
