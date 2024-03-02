package us.smartmc.event.eventscore.instance;

import us.smartmc.event.eventscore.config.EventConfig;

public class ToggleableItem<T extends Enum<T>> implements IToggleableType<T> {

    private final EventConfig config;
    private final String configPath;
    private final Class<T> tClass;

    public ToggleableItem(EventConfig config, String configPath, Class<T> tClass) {
        this.config = config;
        this.configPath = configPath;
        this.tClass = tClass;
    }

    @Override
    public T get() {
        return config.getEnumType(configPath, getEnumClass());
    }

    @Override
    public void set(T value) {
        config.setEnumType(configPath, value);
    }

    @Override
    public Class<T> getEnumClass() {
        return tClass;
    }
}
