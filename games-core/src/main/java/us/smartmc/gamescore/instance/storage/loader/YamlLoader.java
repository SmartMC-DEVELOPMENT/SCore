package us.smartmc.gamescore.instance.storage.loader;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class YamlLoader implements IDataFileLoader {

    @Override
    public Map<String, Object> load(File file) {
        IDataFileLoader.createFiles(file);
        Yaml yaml = new Yaml();
        try {
            InputStream inputStream = new FileInputStream(file);
            return yaml.loadAs(inputStream, HashMap.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
