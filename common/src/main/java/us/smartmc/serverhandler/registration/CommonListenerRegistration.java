package us.smartmc.serverhandler.registration;

import me.imsergioh.jbackend.api.manager.DataProcessorManager;
import us.smartmc.serverhandler.listener.CommandManagerListener;
import us.smartmc.serverhandler.IRegistration;

public class CommonListenerRegistration implements IRegistration {

    @Override
    public void register() {
        DataProcessorManager.register(new CommandManagerListener());
    }
}
