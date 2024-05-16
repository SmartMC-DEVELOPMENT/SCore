package us.smartmc.backend.service;

import us.smartmc.backend.instance.service.BackendService;

public class TestPlayerService extends BackendService {

    @Override
    public void load() {
        super.load();
        System.out.println("Loaded service of TestPlayer!");
    }

    @Override
    public void unload() {
        super.unload();
        System.out.println("Unloaded service of TestPlayer!");
    }
}
