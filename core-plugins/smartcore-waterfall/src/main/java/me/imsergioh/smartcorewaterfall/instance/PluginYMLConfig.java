package me.imsergioh.smartcorewaterfall.instance;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PluginYMLConfig {

    private final File file;

    @Getter
    private Configuration config;

    public PluginYMLConfig(File file) {
        this.file = file;
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        load();
    }

    public void save() {
        try {
            YamlConfiguration.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void load() {
        try {
            config = YamlConfiguration.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
