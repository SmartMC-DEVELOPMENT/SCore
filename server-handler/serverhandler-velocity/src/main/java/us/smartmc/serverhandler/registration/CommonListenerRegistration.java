package us.smartmc.serverhandler.registration;

import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.serverhandler.IRegistration;
import us.smartmc.serverhandler.listener.ACommandManagerListener;

public class CommonListenerRegistration implements IRegistration {

    @Override
    public void register() {
        ConnectionInputManager.registerListeners(new ACommandManagerListener());
    }
}
