package de.nevini.modules.util.unicode.text;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class SmallCapsCommand extends Command {

    private final Map<Character, Character> map;

    public SmallCapsCommand() {
        super(CommandDescriptor.builder()
                .keyword("small-caps")
                .aliases(new String[]{"smallcaps", "sc"})
                .guildOnly(false)
                .node(Node.UTIL_UNICODE_TEXT)
                .minimumBotPermissions(Permissions.TALK)
                .description("converts text to lower case")
                .build());

        HashMap<Character, Character> map = new HashMap<>();
        map.put('a', 'ᴀ');
        map.put('b', 'ʙ');
        map.put('c', 'ᴄ');
        map.put('d', 'ᴅ');
        map.put('e', 'ᴇ');
        map.put('f', 'ꜰ');
        map.put('g', 'ɢ');
        map.put('h', 'ʜ');
        map.put('i', 'ɪ');
        map.put('j', 'ᴊ');
        map.put('k', 'ᴋ');
        map.put('l', 'ʟ');
        map.put('m', 'ᴍ');
        map.put('n', 'ɴ');
        map.put('o', 'ᴏ');
        map.put('p', 'ᴘ');
        map.put('q', 'ꞯ');
        map.put('r', 'ʀ');
        map.put('s', 's');
        map.put('t', 'ᴛ');
        map.put('u', 'ᴜ');
        map.put('v', 'ᴠ');
        map.put('w', 'ᴡ');
        map.put('x', 'x');
        map.put('y', 'ʏ');
        map.put('z', 'ᴢ');
        this.map = Collections.unmodifiableMap(map);
    }

    @Override
    protected void execute(CommandEvent event) {
        String arg = event.getArgument();
        if (StringUtils.isNotBlank(arg)) {
            StringBuilder sb = new StringBuilder();
            for (char c : arg.toCharArray()) {
                sb.append(map.getOrDefault(c, c));
            }
            event.reply(sb.toString(), event::complete);
        }
    }

}
