package us.smartmc.game;

import org.bukkit.WorldCreator;
import us.smartmc.game.listener.MainIslandsListeners;
import us.smartmc.game.listener.TestListeners;
import us.smartmc.skyblock.ISkyBlockAPI;
import us.smartmc.skyblock.instance.SkyBlockServerType;

public class SkyBlockLobby implements ISkyBlockAPI {

    @Override
    public void onEnable() {
        SkyBlockPlugin.registerListeners(new MainIslandsListeners(), new TestListeners());

        new WorldCreator("island-72d86ee6-18a1-4714-a4a2-38d86cd70553").createWorld();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public SkyBlockServerType getBlockModeType() {
        return SkyBlockServerType.SPAWN;
    }
}
