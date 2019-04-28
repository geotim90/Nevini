package de.nevini.services;

import de.nevini.util.Emote;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.Guild;
import org.springframework.stereotype.Service;

@Service
public class EmoteService {

    // TODO Idea: Allow emote customization per guild

    public String getGuildEmote(Guild guild, @NonNull Emote emote) {
        return emote.getUnicode();
    }

}
