package us.smartmc.lobbycosmetics.cosmetic;

import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticActionType;

public abstract class HatCosmetic extends Cosmetic implements IHatCosmetic {

    public HatCosmetic(String id) {
        super(id);
    }

    @Override
    public String getSkullTexture() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTY5NjU4NDIyODIyNCwKICAicHJvZmlsZUlkIiA6ICIwMzcyOTQyZWRhOTQ0MjQ3YWRlODcyZjRjYjQxZTFkZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJiZW5rZXRvbWkiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDk4NGY4MTFjOWQ2NDczYWY2NTMzZTAzMzAxZWM4NzgzMzk0YzIwMjAyYzNjZDU0MjYwZGFhOTNmM2FlMDI5NCIKICAgIH0KICB9Cn0=";
    }

    @Override
    public CosmeticActionType getType() {
        return CosmeticActionType.HAT;
    }
}
