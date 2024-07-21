
package us.smartmc.game;

import org.bukkit.Bukkit;
import us.smartmc.game.listener.MainIslandsListeners;
import us.smartmc.skyblock.ISkyBlockAPI;
import us.smartmc.skyblock.instance.SkyBlockServerType;


public class SkyBlockLobby implements ISkyBlockAPI {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new MainIslandsListeners(), SkyBlockPlugin.getPlugin());
    }

    @Override
    public SkyBlockServerType getBlockModeType() {
        return SkyBlockServerType.SPAWN;
    }


}
