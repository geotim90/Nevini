package de.nevini.modules.util.unicode.morse;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class MorseEncodeCommand extends Command {

    public MorseEncodeCommand() {
        super(CommandDescriptor.builder()
                .keyword("morse-encode")
                .aliases(new String[]{"morseencode", "morse-enc", "morseenc"})
                .guildOnly(false)
                .node(Node.UTIL_UNICODE_MORSE)
                .minimumBotPermissions(Permissions.TALK)
                .description("converts text to morse code")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        String arg = event.getArgument();
        if (StringUtils.isNotBlank(arg)) {
            event.reply(MorseEncoder.encode(arg), event::complete);
        }
    }

}
