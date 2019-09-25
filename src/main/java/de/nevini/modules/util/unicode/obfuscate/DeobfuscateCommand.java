package de.nevini.modules.util.unicode.obfuscate;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DeobfuscateCommand extends Command {

    public DeobfuscateCommand() {
        super(CommandDescriptor.builder()
                .keyword("deobfuscate")
                .guildOnly(false)
                .node(Node.UTIL_UNICODE_OBFUSCATE)
                .minimumBotPermissions(Permissions.TALK)
                .description("deobfuscates text")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        String arg = event.getArgument();
        if (StringUtils.isNotBlank(arg)) {
            event.reply(Obfuscator.deobfuscate(arg), event::complete);
        }
    }

}
