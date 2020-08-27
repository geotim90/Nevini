package de.nevini.modules.util.unicode.text;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ReverseCommand extends Command {

    public ReverseCommand() {
        super(CommandDescriptor.builder()
                .keyword("reverse")
                .guildOnly(false)
                .node(Node.UTIL_UNICODE_TEXT)
                .minimumBotPermissions(Permissions.TALK)
                .description("reverses text")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        String arg = event.getArgument();
        if (StringUtils.isNotBlank(arg)) {
            event.reply(StringUtils.reverse(arg), event::complete);
        }
    }

}
