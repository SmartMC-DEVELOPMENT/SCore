package us.smartmc.backend.instance;

import lombok.Getter;

@Getter
public abstract class BackendCommandExecutor implements IBackendCommandExecutor {

    private final String name;

    public BackendCommandExecutor(String name) {
        this.name = name;
    }
}
