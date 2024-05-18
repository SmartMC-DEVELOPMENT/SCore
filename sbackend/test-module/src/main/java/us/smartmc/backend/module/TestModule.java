package us.smartmc.backend.module;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.module.listener.TestListenerReceiver;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;

@ModulePluginInfo(name = "TestModule", version = "DEV")
public class TestModule extends ModulePlugin {

    public TestModule() {
        super();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("LOADED MODULE TEST " + Material.AIR.name());

        ConnectionInputManager.registerListeners(new TestListenerReceiver());

        ConnectionInputManager.performListener(null, new AsyncPlayerChatEvent(true, null, "asd", Set.of()));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.exit(0);
    }
}
