package main.java.us.smartmc.serverhandler.registration;

import me.imsergioh.jbackend.api.manager.DataProcessorManager;
import main.java.us.smartmc.serverhandler.listener.CommandManagerListener;
import main.java.us.smartmc.serverhandler.IRegistration;

public class CommonListenerRegistration implements IRegistration {

    @Override
    public void register() {
        DataProcessorManager.register(new CommandManagerListener());
    }
}
