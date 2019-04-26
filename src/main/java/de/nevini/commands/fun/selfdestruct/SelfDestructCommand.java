package de.nevini.commands.fun.selfdestruct;

import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.bot.AbstractCommand;
import de.nevini.commands.fun.FunCategory;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SelfDestructCommand extends AbstractCommand {

    public SelfDestructCommand(@Autowired FunCategory category) {
        super("self-destruct", "causes the bot to \"self destruct\"", category);
        this.guildOnly = false;
        this.cooldown = 60;
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE};
        this.aliases = new String[]{"selfdesctruct", "auto-destruct", "autodestruct"};
        this.cooldownScope = CooldownScope.GLOBAL;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("⚠️ Automatic self-destruct sequence initiated!", this::tMinus5);
        if (event.getChannelType() == ChannelType.TEXT) event.getMessage().delete().queue();
    }

    private void tMinus5(Message message) {
        message.editMessage(message.getContentRaw() + "\n\nDetonation will commence in 10 seconds!")
                .queue(this::tMinus3);
    }

    private void tMinus3(Message message) {
        message.editMessage(message.getContentRaw() + "\n\n3...").queueAfter(7, TimeUnit.SECONDS, this::tMinus2);
    }

    private void tMinus2(Message message) {
        message.editMessage(message.getContentRaw() + "\n2...").queueAfter(1, TimeUnit.SECONDS, this::tMinus1);
    }

    private void tMinus1(Message message) {
        message.editMessage(message.getContentRaw() + "\n1...").queueAfter(1, TimeUnit.SECONDS, this::detonation);
    }

    private void detonation(Message message) {
        message.editMessage("\uD83D\uDCA5").queueAfter(1, TimeUnit.SECONDS, this::aftermath);
    }

    private void aftermath(Message message) {
        message.getJDA().getPresence().setStatus(OnlineStatus.INVISIBLE);
        message.delete().queueAfter(30, TimeUnit.SECONDS, e -> recovery(message), e -> recovery(message));
    }

    private void recovery(Message message) {
        message.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
    }

}
