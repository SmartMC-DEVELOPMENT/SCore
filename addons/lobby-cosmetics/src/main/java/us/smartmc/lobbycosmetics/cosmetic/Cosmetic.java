package us.smartmc.lobbycosmetics.cosmetic;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmetic;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class Cosmetic implements ICosmetic {

    private final Set<UUID> activeEntities = new HashSet<>();
    private final String id;

    public Cosmetic(String id) {
        this.id = id;
    }

    @Override
    public void toggleEntity(LivingEntity entity) {
        if (!activeEntities.contains(entity.getUniqueId())) {
            activeEntities.add(entity.getUniqueId());
            onEnableEntity(entity);
            return;
        }
        activeEntities.remove(entity.getUniqueId());
        onDisableEntity(entity);
    }

    @Override
    public String getId() {
        return id;
    }
}
