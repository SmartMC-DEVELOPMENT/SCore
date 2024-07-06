
package us.smartmc.game;

import com.comphenix.protocol.ProtocolLibrary;
import us.smartmc.game.listener.MainIslandsListeners;
import us.smartmc.game.listener.TestListeners;
import us.smartmc.game.util.WorldUtils;
import us.smartmc.skyblock.ISkyBlockAPI;
import us.smartmc.skyblock.instance.SkyBlockServerType;


public class SkyBlockLobby implements ISkyBlockAPI {

    @Override
    public void onEnable() {
        SkyBlockPlugin.registerListeners(new MainIslandsListeners(), new TestListeners());

        WorldUtils.deleteIslandWorlds();
    }

    @Override
    public void onDisable() {
        WorldUtils.deleteIslandWorlds();
    }

    @Override
    public SkyBlockServerType getBlockModeType() {
        return SkyBlockServerType.SPAWN;
    }


}
