package us.smartmc.lobbycosmetics.cosmeticsection;

import org.bukkit.Material;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticActionType;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticSection;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticSectionInfo;

@CosmeticSectionInfo(type = CosmeticActionType.EFFECT, icon = Material.FIREWORK)
public class EffectSection extends CosmeticSection {

    @Override
    public Material getIconMaterial() {
        return Material.FIREWORK;
    }
}
