package us.smartmc.gamescore.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import us.smartmc.gamescore.instance.region.metadata.CustomRegionMetadata;

public class TestCustomRegionMetadataListener extends CustomRegionMetadata {

    public TestCustomRegionMetadataListener() {
        super("DENY@LAVA");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLava(PlayerBucketEmptyEvent event) {
        if (!event.getBucket().name().contains("LAVA")) return;
        if (isActiveMetadata(event.getBlockClicked().getLocation()))
            event.setCancelled(true);
    }
}
