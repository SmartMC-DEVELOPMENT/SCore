package us.smartmc.snowgames.util;

public class DebugUtil {

    private static boolean enabled = false;

    public static void debug(String name, String message) {
        if (!enabled) return;
        System.out.println("[DEBUG - " + name + "] " + message);
    }

    public static void setEnabled(boolean enabled) {
        DebugUtil.enabled = enabled;
    }
}
