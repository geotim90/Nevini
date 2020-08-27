package de.nevini.modules.util.unicode.html;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class HtmlUnescapeCommand extends Command {

    public HtmlUnescapeCommand() {
        super(CommandDescriptor.builder()
                .keyword("html-unescape")
                .aliases(new String[]{"htmlunescape", "html-unesc", "htmlunesc"})
                .guildOnly(false)
                .node(Node.UTIL_UNICODE_HTML)
                .minimumBotPermissions(Permissions.TALK)
                .description("converts text using HTML escape sequences")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        String arg = event.getArgument();
        if (StringUtils.isNotBlank(arg)) {
            event.reply(StringEscapeUtils.unescapeHtml4(arg), event::complete);
        }
    }

}
