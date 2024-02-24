package us.smartmc.lobbycosmetics.instance.cosmetic;

import org.bukkit.Material;

public interface ICosmetic {

    CosmeticActionType getType();

    Material getMaterialType();

    String getId();
}
