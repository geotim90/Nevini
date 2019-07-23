package de.nevini.util.message;

import lombok.NonNull;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.function.Consumer;

public class MessageLineSplitter {

    public static void sendMessage(
            @NonNull MessageChannel channel, @NonNull String content, @NonNull Consumer<? super Message> callback
    ) {
        if (content.length() <= Message.MAX_CONTENT_LENGTH) {
            channel.sendMessage(content).queue(callback);
        } else {
            String remainder = content;
            while (remainder.length() > Message.MAX_CONTENT_LENGTH) {
                String part = remainder.substring(0, remainder.lastIndexOf('\n', Message.MAX_CONTENT_LENGTH));
                remainder = remainder.substring(part.length());
                if (remainder.isEmpty()) {
                    channel.sendMessage(part).queue(callback);
                } else {
                    channel.sendMessage(part).queue();
                }
            }
        }
    }

}
