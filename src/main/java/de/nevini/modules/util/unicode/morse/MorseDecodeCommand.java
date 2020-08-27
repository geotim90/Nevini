package de.nevini.modules.util.unicode.morse;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MorseDecodeCommand extends Command {

    public MorseDecodeCommand() {
        super(CommandDescriptor.builder()
                .keyword("morse-decode")
                .aliases(new String[]{"morsedecode", "morse-dec", "morsedec"})
                .guildOnly(false)
                .node(Node.UTIL_UNICODE_MORSE)
                .minimumBotPermissions(Permissions.TALK)
                .description("converts morse code to text")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        String arg = event.getArgument();
        if (StringUtils.isNotBlank(arg)) {
            event.reply(MorseEncoder.decode(arg), event::complete);
        }
    }

}
