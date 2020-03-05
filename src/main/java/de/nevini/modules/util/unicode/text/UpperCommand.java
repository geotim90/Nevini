package de.nevini.modules.util.unicode.text;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class UpperCommand extends Command {

    public UpperCommand() {
        super(CommandDescriptor.builder()
                .keyword("upper-case")
                .aliases(new String[]{"uppercase", "upper"})
                .guildOnly(false)
                .node(Node.UTIL_UNICODE_TEXT)
                .minimumBotPermissions(Permissions.TALK)
                .description("converts text to upper case")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        String arg = event.getArgument();
        if (StringUtils.isNotBlank(arg)) {
            event.reply(StringUtils.upperCase(arg), event::complete);
        }
    }

}
