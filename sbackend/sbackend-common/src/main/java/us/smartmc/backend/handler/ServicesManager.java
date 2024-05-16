package us.smartmc.backend.handler;

import us.smartmc.backend.instance.service.IBackendService;

import java.util.HashMap;
import java.util.Map;

public class ServicesManager {

    private static final Map<Class<? extends IBackendService>, IBackendService> services = new HashMap<>();

    public static void load(Class<? extends IBackendService> clazz) {
        IBackendService service = get(clazz);
        if (service == null) return;
        service.load();
    }

    public static void unload(Class<? extends IBackendService> clazz) {
        IBackendService service = get(clazz);
        if (service == null) return;
        service.unload();
    }

    public static void registerServices(boolean load, IBackendService... servicesToRegister) {
        for (IBackendService service : servicesToRegister) {
            services.put(service.getClass(), service);
            if (load) service.load();
        }
    }

    public static <T extends IBackendService> T get(Class<T> clazz) {
        return (T) clazz.cast(services.get(clazz));
    }
}
