package us.smartmc.event.eventscore.instance;

import us.smartmc.event.eventscore.util.EnumUtils;

public interface IToggleableType<T extends Enum<T>> {

    T toggleType();
    T getCurrentType();

    default T[] getValues() {
        return EnumUtils.getValues((Class<T>) getClass());
    }
}
