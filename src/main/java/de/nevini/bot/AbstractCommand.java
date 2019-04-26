package de.nevini.bot;

import com.jagrosh.jdautilities.command.Command;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class AbstractCommand extends Command {

    protected AbstractCommand(String name, String help, Category category) {
        this.name = name;
        this.help = help;
        this.category = category;
    }

    protected AbstractCommand(String name, String help, Category category, Command... children) {
        this(name, help, category);
        this.children = children;
        mergeChildProps();
    }

    private void mergeChildProps() {
        this.arguments = mergeChildArguments();
    }

    private String mergeChildArguments() {
        if (children.length > 1) {
            return "( "
                    + Arrays.stream(children)
                    .map(this::joinNameAndArguments)
                    .collect(Collectors.joining(" | "))
                    + " )";
        } else if (children.length == 1) {
            return joinNameAndArguments(children[0]);
        } else {
            return null;
        }
    }

    private String joinNameAndArguments(Command cmd) {
        if (StringUtils.isEmpty(cmd.getArguments())) {
            return cmd.getName();
        } else {
            return cmd.getName() + " " + cmd.getArguments();
        }
    }

}
