package us.smartmc.gamesmanager.util;

public class LogUtils {

    public static void log(Class<?> from, String msg) {
        System.out.println("[" + from.getSimpleName() + "] " + msg);
    }
}
