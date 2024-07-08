package us.smartmc.backend.instance.cache;

import lombok.Getter;

import java.util.Optional;

@Getter
public class CacheObtainedWrapper {

    private final String key;
    private final Object value;

    public CacheObtainedWrapper(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public <T> Optional<T> getOptionalOfClass(Class<T> tClass) {
        if (tClass.isAssignableFrom(value.getClass()) || tClass.equals(value.getClass()) || value.getClass().isAssignableFrom(tClass)) {
            return Optional.of(getOfClass(tClass));
        }
        return Optional.empty();
    }

    public <T> T getOfClass(Class<T> tClass) {
        return tClass.cast(value);
    }

}
