package us.smartmc.core.luckywars.manager;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import us.smartmc.core.luckywars.LuckyWars;
import us.smartmc.core.luckywars.instance.game.GameTemplate;

import java.io.File;
import java.util.Objects;

public class GameTemplatesManager extends ManagerRegistry<String, GameTemplate> {

    private static final LuckyWars plugin = LuckyWars.getPlugin();
    public static final File TEMPLATES_DIR = new File(plugin.getDataFolder() + "/templates");

    @Override
    public void load() {
        if (TEMPLATES_DIR.listFiles() == null) {
            System.out.println("Couldn't load any template because templates dir files is null");
            return;
        }

        for (File file : Objects.requireNonNull(TEMPLATES_DIR.listFiles())) {
            if (!file.getName().endsWith(".json")) continue;
            String name = file.getName().replace(".json", "");
            register(name, GameTemplate.get(name));
        }
    }

    @Override
    public void unload() {

    }

    public static FilePluginConfig getConfig(String name) {
        return new FilePluginConfig(new File(GameTemplatesManager.TEMPLATES_DIR, name + ".json"));
    }

}
