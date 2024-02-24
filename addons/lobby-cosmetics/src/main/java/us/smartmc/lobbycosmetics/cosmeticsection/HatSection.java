package us.smartmc.lobbycosmetics.cosmeticsection;

import org.bukkit.Material;
import us.smartmc.lobbycosmetics.cosmetic.HatCosmetic;
import us.smartmc.lobbycosmetics.hatcosmetic.CakeHat;
import us.smartmc.lobbycosmetics.hatcosmetic.TestHat;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticSection;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticSectionInfo;

@CosmeticSectionInfo(type = CosmeticType.HATS, icon = Material.SKULL_ITEM)
public class HatSection extends CosmeticSection<HatCosmetic> {

    public HatSection() {
        register(HatCosmetic.dynamic("anonymous", "ewogICJ0aW1lc3RhbXAiIDogMTY1NjM0NDA0MTM2MSwKICAicHJvZmlsZUlkIiA6ICJmMjc0YzRkNjI1MDQ0ZTQxOGVmYmYwNmM3NWIyMDIxMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJIeXBpZ3NlbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85MjJlZDY0YWVlOWVjMWUzZDgxM2NiNGI0NWZlMWYwNzBmODEyZGU2MjMyNzFkZjBmOWQ1MWYzMTY4YTk3OGI5IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0="));
        register(new CakeHat(), new TestHat());
    }

    @Override
    public String getSkullTexture() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTY5OTQwMTcxNjgwNiwKICAicHJvZmlsZUlkIiA6ICI4YTg3NGJhNmFiZDM0ZTc5OTljOWM1ODMwYWYyY2NmNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJFeGVjdXRvcnMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTUyODk1N2E0NGRhZTAyNmIxODhiOTM2OGQzODNjNjZjOGFkNTFiMmFmNDkwNWMwYzhjM2Q0ZTYyYjBhZWE1YSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
    }
}
