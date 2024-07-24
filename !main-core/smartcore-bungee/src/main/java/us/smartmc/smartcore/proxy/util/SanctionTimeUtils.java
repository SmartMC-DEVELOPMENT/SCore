package us.smartmc.smartcore.proxy.util;

public class SanctionTimeUtils {

    private int days, hours, minutes, seconds;

    private final TimeUtils timeUtils;

    public SanctionTimeUtils(String label) {
        timeUtils = new TimeUtils();
        label = parse(label);
        for (String arg : label.split(" ")) {

            int amount;
            if ((amount = (amount(arg, "d"))) != -1) {
                timeUtils.addDays(amount);
            }

            if ((amount = (amount(arg, "h"))) != -1) {
                timeUtils.addHours(amount);
            }

            if ((amount = (amount(arg, "m"))) != -1) {
                timeUtils.addMinutes(amount);
            }

            if ((amount = (amount(arg, "s"))) != -1) {
                timeUtils.addSeconds(amount);
            }
        }
    }

    public TimeUtils getTimeUtils() {
        return timeUtils;
    }

    public String parse(String label) {
        for (String arg : label.replaceAll("[0-9]", "").split(" ")) {
            if (arg.startsWith("d")) {
                label = label.replace(arg, "d");
            }
            if (arg.startsWith("h")) {
                label = label.replace(arg, "h");
            }
            if (arg.startsWith("m")) {
                label = label.replace(arg, "m");
            }
            if (arg.startsWith("s")) {
                label = label.replace(arg, "s");
            }
        }

        return label;
    }

    private int amount(String arg, String suffix) {
        if (arg.endsWith(suffix)) {
            arg = arg.replace(suffix, "");
            try {
                int amount = Integer.parseInt(arg);
                return amount;
            } catch (Exception ignore){}
        }
        return -1;
    }

}
