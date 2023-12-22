package us.smartmc.serverhandler.registration;

import us.smartmc.serverhandler.IRegistration;
import us.smartmc.serverhandler.instance.Configuration;

import java.io.File;

public class ConfigRegistration implements IRegistration {

    @Override
    public void register() {
        // Load all configs les go
        if (Configuration.getParent().listFiles() == null) return;

        for (File file : Configuration.getParent().listFiles()) {
            new Configuration(file.getName().replace(".json", ""));
        }
    }
}
