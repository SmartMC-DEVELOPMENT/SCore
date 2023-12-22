package us.smartmc.core.pluginsapi.spigot.instance;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.smartmc.core.pluginsapi.spigot.item.ItemBuilder;
import us.smartmc.core.pluginsapi.util.ChatUtil;
import us.smartmc.core.pluginsapi.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class SpigotAPIConfig {

    private FileConfiguration config;
    private final File file;

    public SpigotAPIConfig(File file) {
        this.file = file;
        FileUtil.setup(file);
        load();
    }

    public void setItem(String path, ItemBuilder builder) {
        try {
            Field field = ItemBuilder.class.getDeclaredField("item");
            field.setAccessible(true);
            ItemStack item = (ItemStack) field.get(builder);
            ItemMeta meta = item.getItemMeta();
            ConfigurationSection section = config.createSection(path);

            section.set("type", item.getType().name());
            if (item.getAmount() > 1) {
                section.set("amount", item.getAmount());
            }
            if (item.getData().getData() != (byte) 0) {
                section.set("data", ((Number) item.getData().getData()).intValue());
            }


            if (meta.getDisplayName() != null) {
                section.set("name", meta.getDisplayName());
            }

            if (meta.getLore() != null && !meta.getLore().isEmpty()) {
                section.set("lore", meta.getLore());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ItemBuilder getItem(Player player, String path) {
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) return null;
        ItemBuilder builder = ItemBuilder.of(Material.valueOf(section.getString("type")));

        if (section.contains("amount")) builder.amount(section.getInt("amount"));
        if (section.contains("data")) builder.data(section.getInt("data"));
        if (section.contains("name")) builder.name(ChatUtil.parse(player, section.getString("name")));
        if (section.contains("lore")) builder.lore(section.getStringList("lore"));
        return builder;
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public void set(String path, Object value) {
        if (value instanceof ItemBuilder) {
            setItem(path, (ItemBuilder) value);
            return;
        }
        config.set(path, value);
    }

    public ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }

    public Map<String, Object> getValues(boolean deep) {
        return config.getValues(deep);
    }

    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }

    public boolean register(String path, Object value) {
        if (config.contains(path)) return false;
        set(path, value);
        return true;
    }

    public boolean replace(String path, Object value) {
        boolean replaced = config.contains(path);
        set(path, value);
        return replaced;
    }

    public boolean isEnabled(String path) {
        return getBoolean(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public Object get(String path) {
        return config.get(path);
    }

    public void load() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
