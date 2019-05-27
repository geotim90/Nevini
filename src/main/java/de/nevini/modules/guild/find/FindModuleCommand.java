package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.StringResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Module;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;

public class FindModuleCommand extends Command {

    private static final StringResolver nameResolver = new StringResolver("name", "name");

    public FindModuleCommand() {
        super(CommandDescriptor.builder()
                .keyword("module")
                .node(Node.GUILD_FIND_MODULE)
                .description("finds moduels by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("[--name] <name>")
                                .description("Any part of the name of modules to look for. The flag is optional if this option is provided first.")
                                .keyword("--name")
                                .aliases(new String[]{"//name"})
                                .build()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        nameResolver.resolveArgumentOrOptionOrInput(event, name -> acceptName(event, name));
    }

    private void acceptName(CommandEvent event, String name) {
        List<Module> modules = Resolvers.MODULE.findSorted(event, name);
        if (modules.isEmpty()) {
            event.reply("I could not find any modules that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            modules.forEach(module -> embed.addField(module.getName(), "", true));
            event.reply(embed, event::complete);
        }
    }

}
