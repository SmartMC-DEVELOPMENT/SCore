package us.smartmc.smartcore.proxy.manager;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.kyori.adventure.bossbar.BossBar;
import net.md_5.bungee.api.chat.BaseComponent;
import us.smartmc.smartcore.proxy.SmartCoreBungeeCord;

import java.io.File;
import java.util.*;

public class HostnameRulesManager {

    private static final Map<String, Set<BossBar>> bossbars = new HashMap<>();

    private static final SmartCoreBungeeCord plugin = SmartCoreBungeeCord.getPlugin();

    private static final Map<String, FilePluginConfig> configs = new HashMap<>();

    public static void load() {
        File dir = new File(plugin.getDataFolder() + "/hostnameRules");
        dir.mkdirs();
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            String name = file.getName();
            if (!name.endsWith(".json")) continue;
            name = name.replace(".json", "");
            FilePluginConfig config = new FilePluginConfig(file).load();
            configs.put(name, config);

            Set<BossBar> hostBars = new HashSet<>();
            for (String text : new HashSet<>(config.getList("bossbars", String.class))) {
                BaseComponent bossbarComponent = ChatUtil.parse(text);
                //hostBars.add(BossBar.bossBar(bossbarComponent, 1.0f, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS, Set.of()));
            }
            bossbars.put(name, hostBars);
        }
    }

    public static List<String> getHostnameRedirections(String key) {
        FilePluginConfig config = get(key);
        if (config == null) return List.of();
        return config.getList("servers", String.class, new ArrayList<>());
    }

    public static Set<BossBar> getBossbars(String key) {
        return bossbars.get(key);
    }

    public static FilePluginConfig get(String key) {
        return configs.get(key);
    }

}
