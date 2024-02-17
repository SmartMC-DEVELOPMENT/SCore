
package us.smartmc.serverhandler.registration;

import us.smartmc.serverhandler.IConfigRegistration;
import us.smartmc.serverhandler.config.DataConfiguration;
import us.smartmc.serverhandler.config.ServerConfiguration;
import us.smartmc.serverhandler.instance.Configuration;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ConfigRegistration implements IConfigRegistration {

    @Override
    public void register() {
        load("/home/network/server-handler/config", ServerConfiguration.class);
        load("/home/network/server-handler", DataConfiguration.class);
    }

    @Override
    public <T extends Configuration<?>> void load(String filePath, Class<T> clazz) {
        File dir = new File(filePath);
        if (dir.listFiles() == null) return;
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (!file.getName().endsWith(".json")) continue;
            try {
                  clazz.getDeclaredConstructor(String.class, String.class).newInstance(filePath, file.getName().replace(".json", ""));
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
