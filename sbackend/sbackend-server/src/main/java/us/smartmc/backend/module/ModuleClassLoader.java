package us.smartmc.backend.module;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;

public class ModuleClassLoader extends URLClassLoader {

    public ModuleClassLoader(URL[] urls) {
        super(urls);
    }

    @Override
    protected void addURL(URL url) {
        super.addURL(url);
    }

    public static void loadPluginJar(File file) {
        try {
            URL jarUrl = file.toURI().toURL();
            ModuleClassLoader loader = getLoader(new URL[]{jarUrl});
            JsonObject jsonObject = readJsonObject(loader, "plugin.json");
            String mainClass = jsonObject.get("main").getAsString();
            Class<?> loadedClass = loader.loadClass(mainClass);
            if (!IModulePlugin.class.isAssignableFrom(loadedClass)) return;
            IModulePlugin plugin = (IModulePlugin) loadedClass.newInstance();
            plugin.onEnable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ModuleClassLoader getLoader(URL[] jarUrl) {
        return new ModuleClassLoader(jarUrl);
    }

    private static JsonObject readJsonObject(ModuleClassLoader loader, String resourcePath)  {
        InputStream jsonStream = loader.getResourceAsStream(resourcePath);
        if (jsonStream == null) {
            System.out.println("Resource not found: " + resourcePath);
            return null;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(jsonStream);
        return JsonParser.parseReader(inputStreamReader).getAsJsonObject();
    }
}
