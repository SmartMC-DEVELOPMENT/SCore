package us.smartmc.smartbot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    protected static final String IP_PATTERN = "((\\*)|((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|((\\*\\.)?([a-zA-Z0-9-]+\\.){0,5}[a-zA-Z0-9-][a-zA-Z0-9-]+\\.[a-zA-Z]{2,63} ?))";
    protected static final String REAL_IP_PATTERN = ".*\\b(play.smartmc.us|smartmc.us|\\w+\\.smartmc.us)\\b.*";

    public static String[] getIpAddresses(String message) {
        final List<String> ipAddresses = new ArrayList<>();

        final Pattern pattern = Pattern.compile(IP_PATTERN);
        final Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            ipAddresses.add(matcher.group(0));
        }
        return ipAddresses.toArray(new String[0]);
    }

    public static boolean hasFakeIpAddress(String message) {
        final String[] ipAddresses = getIpAddresses(message);
        return hasFakeIpAddress(message, ipAddresses);
    }

    public static boolean hasFakeIpAddress(String message, String[] ipAddresses) {
        if (ipAddresses.length == 0) return false;

        final Pattern realAddress = Pattern.compile(REAL_IP_PATTERN);
        for (String ipAddress : ipAddresses) {
            if (!realAddress.matcher(ipAddress).matches()) return true;
        }
        return false;
    }
}
