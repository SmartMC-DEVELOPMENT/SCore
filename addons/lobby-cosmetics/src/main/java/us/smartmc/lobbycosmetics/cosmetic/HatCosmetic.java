package us.smartmc.lobbycosmetics.cosmetic;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;

public abstract class HatCosmetic extends Cosmetic implements IHatCosmetic {

    public HatCosmetic(String id) {
        super(id);
    }

    @Override
    public void onEnableEntity(LivingEntity entity) {
        ItemBuilder itemBuilder = ItemBuilder.of(Material.SKULL_ITEM).data(3)
                .skullTexture(getSkullTexture());
        entity.getEquipment().setHelmet(itemBuilder.get());
    }

    @Override
    public void onDisableEntity(LivingEntity entity) {
        entity.getEquipment().setHelmet(null);
    }

    @Override
    public String getSkullTexture() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTY5NjU4NDIyODIyNCwKICAicHJvZmlsZUlkIiA6ICIwMzcyOTQyZWRhOTQ0MjQ3YWRlODcyZjRjYjQxZTFkZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJiZW5rZXRvbWkiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk4NGY4MTFjOWQ2NDczYWY2NTMzZTAzMzAxZWM4NzgzMzk0YzIwMjAyYzNjZDU0MjYwZGFhOTNmM2FlMDI5NCIKICAgIH0KICB9Cn0=";
    }

    @Override
    public CosmeticType getType() {
        return CosmeticType.HATS;
    }

    public static HatCosmetic dynamic(String id, int cost, String skullTexture) {
        return new HatCosmetic(id) {
            @Override
            public String getSkullTexture() {
                return skullTexture;
            }
            @Override
            public Material getMaterialType() {
                return Material.SKULL_ITEM;
            }

            @Override
            public int getCost() {
                return cost;
            }
        };
    }

}
