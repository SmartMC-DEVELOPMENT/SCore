package us.smartmc.backend.instance.service;

public interface IBackendService {

    void load();
    void unload();

    boolean isLoaded();
}
