package us.smartmc.gamescore.instance.storage.loader;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Map;

public class JsonLoader implements IDataFileLoader {

    @Override
    public Map<String, Object> load(File file) {
        IDataFileLoader.createFiles(file);
        Gson gson = new Gson();
        Map<String, Object> map = null;
        try (FileReader reader = new FileReader(file.getAbsolutePath())) {
            Type mapType = new TypeToken<Map<String, Object>>() {
            }.getType();
            map = gson.fromJson(reader, mapType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
