package de.nevini.modules.core.module;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Module;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

class ModuleGetCommand extends Command {

    ModuleGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.CORE_MODULE_GET)
                .description("displays a list of modules")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MODULE.describe(true, true)
                })
                .details("If a module is provided, this will display a list of all matching modules.\n"
                        + "If no module is provided, this will display a list of all modules.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MODULE.resolveListArgumentOrOptionOrInputIfExists(event, modules -> {
            if (modules == null || modules.isEmpty()) {
                listModules(event, Arrays.asList(Module.values()));
            } else {
                listModules(event, modules);
            }
        });
    }

    private void listModules(CommandEvent event, List<Module> modules) {
        EmbedBuilder builder = event.createGuildEmbedBuilder();
        for (Module module : modules) {
            builder.addField(module.getName(), event.getModuleService().isModuleActive(event.getGuild(), module)
                    ? CommandReaction.OK.getUnicode()
                    : CommandReaction.DISABLED.getUnicode(), true);
        }
        event.reply(builder, event::complete);
    }

}
