package us.smartmc.lobbycosmetics.cosmeticsection;

import org.bukkit.Material;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticActionType;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticSection;

public class UnknownSection extends CosmeticSection {

    public UnknownSection() {
        super(CosmeticActionType.HAT);
    }

    @Override
    public Material getIconMaterial() {
        return Material.SKULL_ITEM;
    }

    @Override
    public String getSkullTexture() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTYyMjgzMTk1Nzc1MywKICAicHJvZmlsZUlkIiA6ICIwZjczMDA3NjEyNGU0NGM3YWYxMTE1NDY5YzQ5OTY3OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPcmVfTWluZXIxMjMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTliNDUyOWIxMTI5ZWU5ZDliZmRkNDYwY2ZmMDQwOGMwMzJlOTY2NWZhZTMzYTViM2ZlMDNjNWE5YTVhMzE4MCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
    }
}
