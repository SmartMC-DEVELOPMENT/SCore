package us.smartmc.event.eventscore.instance;

public interface IToggleableType<T extends Enum<?>> {

    T get();

    default void toggle() {
        T currentValue = get();
        T[] values = getEnums(getEnumClass());
        int length = values.length;
        int currentIndex = 0;
        while (!currentValue.equals(values[currentIndex])) {
            currentIndex++;
        }
        int nextIndex = currentIndex == length - 1 ? 0 : currentIndex + 1;
        T nextValue = values[nextIndex];
        set(nextValue);
    }

    void set(T value);

    Class<T> getEnumClass();

    static <T extends Enum<?>> T[] getEnums(Class<T> enumClass) {
        return enumClass.getEnumConstants();
    }
}
