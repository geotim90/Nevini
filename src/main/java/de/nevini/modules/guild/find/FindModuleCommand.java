package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.common.ModuleResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Module;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;

public class FindModuleCommand extends Command {

    public FindModuleCommand() {
        super(CommandDescriptor.builder()
                .keyword("module")
                .node(Node.GUILD_FIND_MODULE)
                .description("finds moduels by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        ModuleResolver.describe().build()
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
