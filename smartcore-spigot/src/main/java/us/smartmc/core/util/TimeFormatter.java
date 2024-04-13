package us.smartmc.core.util;

import me.imsergioh.pluginsapi.language.Language;
import us.smartmc.core.messages.GeneralMessages;

public class TimeFormatter {

    public static String formatDurationUntil(Language language, long futureTimestampInSeconds) {
        long durationInSeconds = futureTimestampInSeconds - (System.currentTimeMillis() / 1000);
        StringBuilder timeStringBuilder = new StringBuilder();

        // Calculando y construyendo la cadena de tiempo
        addTimeUnitString(language, timeStringBuilder, durationInSeconds / 31536000, "year"); // 365 * 24 * 60 * 60
        durationInSeconds %= 31536000;
        addTimeUnitString(language, timeStringBuilder, durationInSeconds / 2592000, "month"); // 30 * 24 * 60 * 60
        durationInSeconds %= 2592000;
        addTimeUnitString(language, timeStringBuilder, durationInSeconds / 604800, "week"); // 7 * 24 * 60 * 60
        durationInSeconds %= 604800;
        addTimeUnitString(language, timeStringBuilder, durationInSeconds / 86400, "day"); // 24 * 60 * 60
        durationInSeconds %= 86400;
        addTimeUnitString(language, timeStringBuilder, durationInSeconds / 3600, "hour"); // 60 * 60
        durationInSeconds %= 3600;
        addTimeUnitString(language, timeStringBuilder, durationInSeconds / 60, "minute");
        durationInSeconds %= 60;
        addTimeUnitString(language, timeStringBuilder, durationInSeconds, "second");

        return timeStringBuilder.toString().trim();
    }

    private static void addTimeUnitString(Language language, StringBuilder sb, long timeValue, String timeUnit) {
        if (timeValue > 0) {
            String path = timeUnit + (timeValue > 1 ? "s" : "");
            sb.append(timeValue).append(" ")
                    .append(GeneralMessages.getString(language, path))
                    .append(", ");
        }
    }

}
