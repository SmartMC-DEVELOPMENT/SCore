package us.smartmc.addon;

import us.smartmc.addon.handler.ChatModeHandler;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;

@AddonInfo(name = "chat-module", version = "DEV")
public class ChatModule extends AddonPlugin {

    private static ChatModeHandler handler;

    @Override
    public void start() {
        System.out.println("Started addon " + getInfo().name() + " v" + getInfo().version());
        new ChatModeHandler(this);
    }

    @Override
    public void stop() {
        System.out.println("Stopped addon " + getInfo().name() + " v" + getInfo().version());
    }
}
