package us.smartmc.survival.util;

import java.util.function.Function;

public class VariableUtil {

    public static String replace(String message, String token, Function<String, String> replaceFunction) {
        if (message.contains(token)) {
            return message.replace(token, replaceFunction.apply(token));
        }
        return message;
    }
}
