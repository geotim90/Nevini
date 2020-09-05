package de.nevini.modules.util.find;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Module;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

class FindModuleCommand extends Command {

    FindModuleCommand() {
        super(CommandDescriptor.builder()
                .keyword("module")
                .aliases(new String[]{"modules"})
                .guildOnly(false)
                .node(Node.UTIL_FIND_MODULE)
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
