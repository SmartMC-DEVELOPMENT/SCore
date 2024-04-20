package us.smartmc.backend.util;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConsoleUtil {

    private static final Map<String, String> formattingColors = new HashMap<>();
    private static final String PROMPT = ">> ";
    private static Terminal terminal;
    private static LineReader lineReader;

    static {
        formattingColors.put("&0", "\u001B[0;30m");
        formattingColors.put("&1", "\u001B[0;34m");
        formattingColors.put("&2", "\u001B[0;32m");
        formattingColors.put("&3", "\u001B[0;36m");
        formattingColors.put("&4", "\u001B[0;31m");
        formattingColors.put("&5", "\u001B[0;35m");
        formattingColors.put("&6", "\u001B[0;33m");
        formattingColors.put("&7", "\u001B[0;37m");
        formattingColors.put("&8", "\u001B[0;90m");
        formattingColors.put("&9", "\u001B[0;94m");
        formattingColors.put("&a", "\u001B[0;92m");
        formattingColors.put("&b", "\u001B[0;96m");
        formattingColors.put("&c", "\u001B[0;91m");
        formattingColors.put("&d", "\u001B[0;95m");
        formattingColors.put("&e", "\u001B[0;93m");
        formattingColors.put("&f", "\u001B[0;97m");
        formattingColors.put("&r", "\u001B[0m");

        try {
            terminal = TerminalBuilder.builder().system(true).build();
            lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(new NullCompleter())
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readLine() {
        try {
            return lineReader.readLine(PROMPT);
        } catch (UserInterruptException e) {
            return null;
        }
    }

    public static void print(String message) {
        for (String key : formattingColors.keySet()) {
            message = message.replace(key, formattingColors.get(key));
        }
        terminal.writer().println(message);
        terminal.writer().flush();
        terminal.writer().print(PROMPT);
        terminal.writer().flush();
    }
}
