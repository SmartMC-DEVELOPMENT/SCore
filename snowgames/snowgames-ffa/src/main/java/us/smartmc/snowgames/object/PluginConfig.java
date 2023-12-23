package us.smartmc.snowgames.object;

import me.imsergioh.pluginsapi.item.ItemBuilder;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PluginConfig {

    private final File file;

    private FileConfiguration config;

    public PluginConfig(String filePath) {
        file = new File(filePath);
        reload();
    }

    public void registerMultiLanguageItem(String configPath, ItemBuilder builder) {
        for (Language language : Language.values()) {
            registerItemConfig(language, configPath, builder);
        }
    }

    public ItemBuilder getItemConfig(Language language, String configPath) {
        String path = "item." + configPath;
        Material material = Material.valueOf(getString(path + ".material"));
        int amount = getInt(path + ".amount");

        path = language.name() + "." + configPath;
        String name = null;
        List<String> lore = null;
        if (contains(path + ".name")) {
            name = getString(path + ".name");
        }

        if (contains(path + ".lore")) {
            lore = getStringList(path + ".lore");
        }
        ItemBuilder builder = ItemBuilder.of(material).amount(amount);

        if (name != null) builder.name(name);
        if (lore != null) builder.lore(lore);
        return builder;
    }

    public ConfigurationSection registerItemConfig(Language language, String configPath, ItemBuilder builder) {
        ItemStack item = builder.get();
        ItemMeta meta = item.getItemMeta();

        String path = "item." + configPath;

        registerDefault(path + ".material", item.getType().name());
        registerDefault(path + ".amount", item.getAmount());

        path = language.name() + "." + configPath;

        if (meta != null) {
            registerDefault(path + ".name", meta.getDisplayName());
            registerDefault(path + ".lore", meta.getLore());
        }
        return config.getConfigurationSection(configPath);
    }

    public boolean registerDefault(String path, Object value) {
        if (config.contains(path)) return false;
        if (value == null) return false;
        set(path, value);
        return true;
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public void set(String path, Object value) {
        if (value == null) return;
        config.set(path, value);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public Object get(String path) {
        return config.get(path);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

}
