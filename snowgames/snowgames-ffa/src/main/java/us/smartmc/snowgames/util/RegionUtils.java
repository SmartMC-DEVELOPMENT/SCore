package us.smartmc.snowgames.util;

import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.regions.RegionConfig;
import us.smartmc.core.regions.controller.PlayerRegionSubscriber;

import java.util.Set;

public class RegionUtils {

    public static boolean isAtSpawn(Player player) {
        SmartCorePlayer sPlayer = SmartCorePlayer.get(player);
        PlayerRegionSubscriber subscriber = sPlayer.getPlayerRegionSubscriber();
        return !subscriber.getRegionsAt().isEmpty();
    }

    public static Set<RegionConfig> getRegionsAt(Player player) {
        SmartCorePlayer sPlayer = SmartCorePlayer.get(player);
        PlayerRegionSubscriber subscriber = sPlayer.getPlayerRegionSubscriber();
        return subscriber.getRegionsAt();
    }
}
