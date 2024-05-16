package us.smartmc.backend.instance.service;

import lombok.Getter;

@Getter
public abstract class BackendService implements IBackendService {

    private boolean loaded;

    @Override
    public void load() {
        loaded = true;
    }

    @Override
    public void unload() {
        loaded = false;
    }
}
