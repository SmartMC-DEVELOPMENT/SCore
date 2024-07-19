package us.smartmc.core.util;

public class EnumUtil {

    public static <E extends Enum<E>> E getNextEnumValue(E currentValue) {
        E[] enumConstants = currentValue.getDeclaringClass().getEnumConstants();
        int currentIndex = currentValue.ordinal();
        int nextIndex = (currentIndex + 1) % enumConstants.length;
        return enumConstants[nextIndex];
    }
}
