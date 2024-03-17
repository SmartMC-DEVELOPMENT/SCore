package us.smartmc.lobbycosmetics.hatcosmetic;

import org.bukkit.Material;
import us.smartmc.lobbycosmetics.cosmetic.HatCosmetic;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmetic;

public class CakeHat extends HatCosmetic implements ICosmetic {

    public CakeHat() {
        super("cake");
    }

    @Override
    public String getSkullTexture() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTYyNTc2NzQ5NTU4NSwKICAicHJvZmlsZUlkIiA6ICIyNDBiYThlMzNlN2M0YzE0ODhiNzJmYmU1Njg2ZjhlNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJQbGF4Q3JhZnRzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzc3NGYyNTRhZGIyY2Q3NDlmYTQ1YjM3MWIyMTZlZGNhZWQ0ZTM5MjZhNGI1YmFiNTkyODAxMmI1ZjFjODgyNTAiCiAgICB9CiAgfQp9";
    }

    @Override
    public int getCost() {
        return 1000;
    }

    @Override
    public Material getMaterialType() {
        return Material.PLAYER_HEAD;
    }
}
