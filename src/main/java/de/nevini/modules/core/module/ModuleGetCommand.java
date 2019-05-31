package de.nevini.modules.core.module;

import de.nevini.command.*;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Module;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class ModuleGetCommand extends Command {

    public ModuleGetCommand() {
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
        EmbedBuilder builder = event.createEmbedBuilder();
        for (Module module : modules) {
            builder.addField(module.getName(), event.getModuleService().isModuleActive(event.getGuild(), module)
                    ? CommandReaction.OK.getUnicode()
                    : CommandReaction.DISABLED.getUnicode(), true);
        }
        event.reply(builder, event::complete);
    }

}
