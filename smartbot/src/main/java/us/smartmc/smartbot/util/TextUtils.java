package us.smartmc.smartbot.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextUtils {

    public static String getFormattedLogDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }
}
