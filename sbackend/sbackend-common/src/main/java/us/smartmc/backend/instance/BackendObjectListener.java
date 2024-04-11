package us.smartmc.backend.instance;

import lombok.Getter;

@Getter
public abstract class BackendObjectListener<F> implements IBackendObjectListener<F> {

    private final Class<?> typeClass;

    public BackendObjectListener(Class<?> typeClass) {
        this.typeClass = typeClass;
    }
}
