package us.smartmc.lobbycosmetics.hatcosmetic;

import org.bukkit.Material;
import us.smartmc.lobbycosmetics.cosmetic.HatCosmetic;

public class TestHat extends HatCosmetic {

    public TestHat() {
        super("test");
    }

    @Override
    public String getSkullTexture() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTY5NzIwMjE2MjQyNywKICAicHJvZmlsZUlkIiA6ICI1NzgzZWMxNDgxMDI0ZDJmOTk4N2JhNGZhNWNlMmFmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJCbHVlUGhlbml4NDMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDg0YjUzYjQyNTNkMWU2YmI0OTEzMmRhMjg2YTE3MTE5N2M4N2ZhMzk3ZjFiNThiNWM2YjEyYmQ2NjVjNDY1OSIKICAgIH0KICB9Cn0=";
    }

    @Override
    public Material getMaterialType() {
        return Material.SKULL_ITEM;
    }
}
