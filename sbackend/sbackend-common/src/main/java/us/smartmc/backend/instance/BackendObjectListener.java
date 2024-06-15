package us.smartmc.backend.instance;

import lombok.Getter;

import java.lang.reflect.ParameterizedType;

@Getter
public abstract class BackendObjectListener<F> implements IBackendObjectListener<F> {

    private final Class<?> typeClass;

    public BackendObjectListener() {
        this.typeClass = (Class<F>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
