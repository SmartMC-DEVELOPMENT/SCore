package us.smartmc.backend.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleClassLoader extends URLClassLoader {

    private final static Map<String, ModulePluginInfo> modulesInfo = new HashMap<>();
    private final static Map<String, IModulePlugin> loadedModules = new HashMap<>();

    public ModuleClassLoader(URL[] urls) {
        super(urls);
    }

    @Override
    protected void addURL(URL url) {
        super.addURL(url);
    }

    private static String loadModuleInfo(File file) throws Exception {
        URL jarUrl = file.toURI().toURL();
        ModuleClassLoader loader = getLoader(new URL[]{jarUrl});
        JsonObject jsonObject = readJsonObject(loader, "plugin.json");
        String mainClass = jsonObject.get("main").getAsString();
        String name = jsonObject.get("name").getAsString();
        JsonArray dependenciesArray = jsonObject.has("dependencies") ? jsonObject.getAsJsonArray("dependencies") : new JsonArray();
        List<String> dependencies = new ArrayList<>();
        for (JsonElement dependency : dependenciesArray) {
            dependencies.add(dependency.getAsString());
        }
        modulesInfo.put(name, new ModulePluginInfo(file, mainClass, dependencies));
        return mainClass;
    }

    public static void loadModulesJars(File[] files) {
        for (File file : files) {
            if (file.getName().endsWith(".jar")) {
                try {
                    String infoId = loadModuleInfo(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        for (String moduleName : modulesInfo.keySet()) {
            try {
                loadModuleWithDependencies(moduleName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadModuleWithDependencies(String moduleName) throws Exception {
        if (loadedModules.containsKey(moduleName)) {
            return; // Already loaded
        }
        ModulePluginInfo moduleInfo = modulesInfo.get(moduleName);
        System.out.println("TRIED TO OBTAIN MODULE INFO OF " + moduleName);
        for (String dependency : moduleInfo.getDependencies()) {
            loadModuleWithDependencies(dependency); // Ensure dependencies are loaded first
        }
        loadPluginJar(moduleInfo.getFile());
    }


    public static void loadPluginJar(File file) {
        try {
            URL jarUrl = file.toURI().toURL();
            ModuleClassLoader loader = getLoader(new URL[]{jarUrl});
            JsonObject jsonObject = readJsonObject(loader, "plugin.json");
            String mainClass = jsonObject.get("main").getAsString();
            Class<?> loadedClass = loader.loadClass(mainClass);
            if (!IModulePlugin.class.isAssignableFrom(loadedClass)) return;
            IModulePlugin plugin = (IModulePlugin) loadedClass.getDeclaredConstructor().newInstance();
            plugin.onEnable();
            loadedModules.put(mainClass, plugin); // Register loaded module
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
