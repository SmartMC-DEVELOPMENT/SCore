package us.smartmc.lobbycosmetics.instance.cosmetic;

import org.bukkit.Material;

public @interface CosmeticSectionInfo {

    CosmeticActionType type();
    Material icon();
}
