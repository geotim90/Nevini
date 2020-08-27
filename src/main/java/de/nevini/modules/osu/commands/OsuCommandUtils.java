package de.nevini.modules.osu.commands;

import de.nevini.core.command.CommandEvent;
import de.nevini.modules.admin.game.data.GameData;
import de.nevini.modules.guild.ign.services.IgnService;
import de.nevini.modules.osu.resolvers.OsuUserResolver;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.ChannelType;
import org.apache.commons.lang3.StringUtils;

public class OsuCommandUtils {

    public static @NonNull OsuUserResolver.OsuUserOrMember getCurrentUserOrMember(@NonNull CommandEvent event) {
        return event.isFromType(ChannelType.TEXT)
                ? new OsuUserResolver.OsuUserOrMember(event.getMember())
                : new OsuUserResolver.OsuUserOrMember(event.getAuthor());
    }

    public static String resolveUserName(
            @NonNull OsuUserResolver.OsuUserOrMember user,
            @NonNull IgnService ignService,
            @NonNull GameData game
    ) {
        if (user.getMember() != null) {
            return StringUtils.defaultIfEmpty(
                    ignService.getInGameName(user.getMember(), game),
                    user.getMember().getEffectiveName()
            );
        } else if (user.getUser() != null) {
            return user.getUser().getName();
        } else {
            return user.getOsuUser();
        }
    }
}
