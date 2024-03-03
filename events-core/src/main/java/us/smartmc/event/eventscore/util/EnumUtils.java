package us.smartmc.event.eventscore.util;

public class EnumUtils {

    public static <T extends Enum<T>> T[] getValues(Class<T> tClass) {
        return tClass.getEnumConstants();
    }

    public static <T extends Enum<T>> T getNextValue(Class<T> tClass, T currentValue) {
        T[] values = tClass.getEnumConstants(); // Directly get the enum constants
        int currentIndex = 0;
        for (; currentIndex < values.length; currentIndex++) {
            if (values[currentIndex] == currentValue) {
                break; // Found the current value, break the loop
            }
        }
        // Increment the index to get the next value, wrap around if necessary
        currentIndex = (currentIndex + 1) % values.length;
        return values[currentIndex];
    }
}
