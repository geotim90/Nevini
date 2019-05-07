package de.nevini.modules.core.module;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleGetCommand extends Command {

    public ModuleGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .module(Module.CORE)
                .node(Node.CORE_MODULE_GET)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("displays modules")
                .syntax("[<module>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        if (StringUtils.isEmpty(event.getArgument())) {
            listModules(event, Arrays.asList(Module.values()));
        } else {
            listModules(event, event.getModuleService().findModules(event.getArgument()).stream()
                    .sorted(Comparator.comparing(Module::ordinal)).collect(Collectors.toList()));
        }
    }

    private void listModules(CommandEvent event, List<Module> modules) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(event.getGuild().getSelfMember().getColor());
        builder.setAuthor(event.getGuild().getName(), null, event.getGuild().getIconUrl());
        for (Module module : modules) {
            builder.addField(module.getName(), event.getModuleService().isModuleActive(event.getGuild(), module)
                    ? CommandReaction.OK.getUnicode()
                    : CommandReaction.DISABLED.getUnicode(), true);
        }
        event.reply(builder);
    }

}
