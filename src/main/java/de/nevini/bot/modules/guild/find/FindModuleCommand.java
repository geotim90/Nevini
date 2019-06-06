package de.nevini.bot.modules.guild.find;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Module;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandOptionDescriptor;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;

public class FindModuleCommand extends Command {

    public FindModuleCommand() {
        super(CommandDescriptor.builder()
                .keyword("module")
                .node(Node.GUILD_FIND_MODULE)
                .description("finds modules by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MODULE.describe(true, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MODULE.resolveListArgumentOrOptionOrInput(event, modules -> acceptModules(event, modules));
    }

    private void acceptModules(CommandEvent event, List<Module> modules) {
        if (modules.isEmpty()) {
            event.reply("I could not find any modules that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            modules.forEach(module -> embed.addField(module.getName(), "", true));
            event.reply(embed, event::complete);
        }
    }

}
