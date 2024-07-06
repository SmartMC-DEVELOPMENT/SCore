package us.smartmc.backend.service.social;

import lombok.Getter;
import us.smartmc.backend.handler.ServicesManager;
import us.smartmc.backend.instance.service.BackendService;

@Getter
public class SocialServices extends BackendService {

    private SocialMessagesService messagesService;

    @Override
    public void load() {
        super.load();
        ServicesManager.registerServices(true, messagesService);
    }

    @Override
    public void unload() {
        super.unload();
        System.out.println("Unloaded service of TestPlayer!");
    }

}
