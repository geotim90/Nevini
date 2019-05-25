package de.nevini.listeners;

import de.nevini.services.common.IgnService;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.RichPresence;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class OsuListener {

    private final IgnService ignService;

    public OsuListener(
            @Autowired IgnService ignService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.ignService = ignService;
        eventDispatcher.subscribe(UserUpdateGameEvent.class, this::onUserUpdateGame);
    }

    private void onUserUpdateGame(UserUpdateGameEvent e) {
        if (!e.getUser().isBot()) {
            processUserGame(e.getUser(), e.getOldGame());
            processUserGame(e.getUser(), e.getNewGame());
        }
    }

    private void processUserGame(User user, Game game) {
        if (game != null && game.isRich() && "osu!".equals(game.getName())) {
            RichPresence presence = game.asRichPresence();
            // osu! presence contains the in-game name
            if (presence.getLargeImage() != null) {
                String text = ObjectUtils.defaultIfNull(presence.getLargeImage().getText(), "");
                Matcher matcher = Pattern.compile("(.+) \\(rank #[\\d,]+\\)").matcher(text);
                if (matcher.matches()) {
                    ignService.setIgn(user, presence.getApplicationIdLong(), matcher.group(1));
                }
            }
        }

    }

}
