package us.smartmc.lobbycosmetics.cosmeticsection;

import org.bukkit.Material;
import us.smartmc.lobbycosmetics.cosmetic.HatCosmetic;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticActionType;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticSection;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticSectionInfo;

@CosmeticSectionInfo(type = CosmeticActionType.HAT, icon = Material.SKULL_ITEM)
public class HatSection extends CosmeticSection<HatCosmetic> {

    @Override
    public String getSkullTexture() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTY5OTQwMTcxNjgwNiwKICAicHJvZmlsZUlkIiA6ICI4YTg3NGJhNmFiZDM0ZTc5OTljOWM1ODMwYWYyY2NmNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJFeGVjdXRvcnMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTUyODk1N2E0NGRhZTAyNmIxODhiOTM2OGQzODNjNjZjOGFkNTFiMmFmNDkwNWMwYzhjM2Q0ZTYyYjBhZWE1YSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
    }
}
