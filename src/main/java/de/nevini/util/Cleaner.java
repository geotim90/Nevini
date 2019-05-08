package de.nevini.util;

import net.dv8tion.jda.core.entities.Message;

import static de.nevini.util.Functions.ignore;

public class Cleaner {

    public static void tryDelete(Message message) {
        try {
            message.delete().queue(ignore(), ignore());
        } catch (Exception ignore) {
        }
    }

}
