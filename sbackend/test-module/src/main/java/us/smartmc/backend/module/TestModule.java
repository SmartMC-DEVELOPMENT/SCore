package us.smartmc.backend.module;

import org.bukkit.Material;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.module.command.ChatCommand;
import us.smartmc.backend.module.listener.TestListenerReceiver;


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
        ConnectionInputManager.registerCommands(new ChatCommand());

        //ConnectionInputManager.unregisterCommand("chat");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.exit(0);
    }
}
