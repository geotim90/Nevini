package de.nevini.docs;

import de.nevini.command.Command;
import de.nevini.command.CommandContext;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.scope.Module;
import de.nevini.util.Formatter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DocsGenerator {

    public DocsGenerator(
            @Value("${docs.generate.path:#{null}}") String path,
            @Autowired CommandContext commandContext
    ) {
        if (StringUtils.isEmpty(path)) {
            log.debug("No path set -> not generating any docs.");
            return;
        }

        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            log.error("Path for generated docs is not a valid directory: {}", path);
            return;
        }

        List<Command> commands = commandContext.getCommands().values().stream()
                .sorted(Comparator.comparing(Command::getKeyword))
                .distinct().collect(Collectors.toList());

        for (Module module : Module.values()) {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(dir, module.getName() + ".md")), StandardCharsets.UTF_8
            )))) {
                generateModuleDoc(module,
                        commands.stream().filter(c -> module.equals(c.getModule())).collect(Collectors.toList()), out);
            } catch (IOException e) {
                log.error("Error while generating docs: ", e);
            }
        }
    }

    private void generateModuleDoc(Module module, List<Command> commands, PrintWriter out) {
        // module header
        out.println("# Module: `" + module.getName() + "`");
        out.println();
        out.println(module.getDescription());

        // top level command list
        out.println();
        out.println("Command | Description");
        out.println("--------|------------");
        for (Command command : commands) {
            out.println("[" + command.getKeyword() + "](#command-" + command.getKeyword() + ") | "
                    + command.getDescription());
        }

        // commands
        for (Command command : commands) {
            generateCommandDoc(command, command.getKeyword(), out);
        }
    }

    private void generateCommandDoc(Command command, String chain, PrintWriter out) {
        // command header
        out.println();
        out.println("## Command: `" + command.getKeyword() + "`");

        // command list
        out.println();
        out.println("Command | Description");
        out.println("--------|------------");
        out.println("[" + chain + "](#command-" + chain.replace(' ', '-') + ") | "
                + command.getDescription());
        for (Command child : command.getChildren()) {
            out.println("[" + chain + " " + child.getKeyword() + "](#command-"
                    + chain.replace(' ', '-') + "-" + child.getKeyword() + ") | "
                    + child.getDescription());
        }

        // option list
        if (command.getOptions().length > 0) {
            out.println();
            out.println("Option | Description");
            out.println("-------|------------");
            for (CommandOptionDescriptor option : command.getOptions()) {
                out.println(option.getSyntax().replaceAll("([\\[\\]<>])", "\\\\$1") + " | "
                        + option.getDescription());
            }
        }

        // details
        if (StringUtils.isNotEmpty(command.getDetails())) {
            out.println();
            out.println(command.getDetails()
                    .replace("\n\n", System.lineSeparator() + System.lineSeparator())
                    .replace("\n", "<br>" + System.lineSeparator()));
        }

        // permission node
        if (command.getNode() != null) {
            out.println();
            String[] permissions = Arrays.stream(command.getNode().getDefaultPermissions())
                    .map(Permission::getName).toArray(String[]::new);
            if (permissions.length > 0) {
                out.print("By default, you need the **");
                if (permissions.length == 1) {
                    out.print(permissions[0] + "** permission");
                } else {
                    out.print(Formatter.join(permissions, "**, **", "** and **") + "** permissions");
                }
                out.println(" to execute this command.");
            }
            out.println("Permission overrides may be applied on node **" + command.getNode().getNode() + "**.");
        }

        // aliases
        if (command.getAliases().length > 0
                || Arrays.stream(command.getOptions()).anyMatch(e -> e.getAliases().length > 0)
        ) {
            out.println();
            out.println("Keyword | Aliases");
            out.println("--------|--------");
            if (command.getAliases().length > 0) {
                out.println(command.getKeyword() + " | " + StringUtils.join(command.getAliases(), "<br>"));
            }
            for (CommandOptionDescriptor option : command.getOptions()) {
                if (option.getAliases().length > 0) {
                    out.println(option.getKeyword() + " | " + StringUtils.join(option.getAliases(), "<br>"));
                }
            }
        }

        // children
        for (Command child : command.getChildren()) {
            generateCommandDoc(child, chain + ' ' + child.getKeyword(), out);
        }
    }

}
