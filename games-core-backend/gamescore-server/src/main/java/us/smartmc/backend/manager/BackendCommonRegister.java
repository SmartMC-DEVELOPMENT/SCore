package us.smartmc.backend.manager;

import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.listener.SaveCuboidListener;

public class BackendCommonRegister {

    public static void register() {
        ConnectionInputManager.registerListeners(new SaveCuboidListener());
    }
}
