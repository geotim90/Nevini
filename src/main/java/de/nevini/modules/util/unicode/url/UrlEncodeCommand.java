package de.nevini.modules.util.unicode.url;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class UrlEncodeCommand extends Command {

    public UrlEncodeCommand() {
        super(CommandDescriptor.builder()
                .keyword("url-encode")
                .aliases(new String[]{"urlencode", "url-enc", "urlenc"})
                .guildOnly(false)
                .node(Node.UTIL_UNICODE_URL)
                .minimumBotPermissions(Permissions.TALK)
                .description("converts text using URL escape sequences")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        String arg = event.getArgument();
        if (StringUtils.isNotBlank(arg)) {
            try {
                URLEncoder.encode(arg, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                log.warn("Unexpected exception", e);
            }
        }
    }

}
