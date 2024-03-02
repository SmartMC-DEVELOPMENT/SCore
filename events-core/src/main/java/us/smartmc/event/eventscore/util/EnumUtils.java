package us.smartmc.event.eventscore.util;

public class EnumUtils {

    public static <T extends Enum<T>> T[] getValues(Class<T> tClass) {
        return tClass.getEnumConstants();
    }

    public static <T extends Enum<T>> T getNextValue(Class<T> tClass, T currentValue) {
        T[] values = getValues(tClass);
        int currentIndex = 0;
        while (!(values[currentIndex] == currentValue)) {
            currentIndex++;
        }
        return currentIndex >= values.length - 1 ? values[0] : values[currentIndex];
    }
}
