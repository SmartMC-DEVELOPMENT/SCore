package us.smartmc.core.util;

public class VariableUtil {

    public static String replace(String message, String entry, String variable) {
        if (message == null || entry == null) return message;
        if (message.contains(entry)) {
            message = message.replace(entry, variable);
        }
        return message;
    }
}
