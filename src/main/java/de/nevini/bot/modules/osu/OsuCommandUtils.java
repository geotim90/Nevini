package de.nevini.bot.modules.osu;

import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.db.game.GameData;
import de.nevini.bot.resolvers.osu.OsuUserResolver;
import de.nevini.bot.services.common.IgnService;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.ChannelType;
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
