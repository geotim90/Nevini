package de.nevini.modules.core.module;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import de.nevini.resolvers.Resolvers;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class ModuleGetCommand extends Command {

    public ModuleGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.CORE_MODULE_GET)
                .description("displays modules")
                .syntax("[ [--module] <module> ]")
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
        event.reply(builder, ignore -> event.complete());
    }

}
