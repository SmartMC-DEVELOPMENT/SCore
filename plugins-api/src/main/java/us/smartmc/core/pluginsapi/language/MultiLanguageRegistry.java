package us.smartmc.core.pluginsapi.language;

import us.smartmc.core.pluginsapi.handler.LanguagesHandler;
import us.smartmc.core.pluginsapi.util.ChatUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MultiLanguageRegistry {

    private static final HashMap<String, String> cache = new HashMap<>();

    private final String name;

    public MultiLanguageRegistry(String name, Consumer<LanguageMessagesHolder> holder) {
        this.name = name;
        LanguagesHandler.forEach(languageHolder -> {
            holder.accept(languageHolder.register(name));
        });
        cache.put(name, this.getClass().getName());
        System.out.println("Cache put by class name " + this.getClass().getName());
    }

    public String get(Language language, String path, Object... args) {
        return ChatUtil.parse(LanguagesHandler.get(language).get(name).getString(path), args);
    }

    public <Player> String get(Player player, Language language, String path, Object... args) {
        return ChatUtil.parse(player, LanguagesHandler.get(language).get(name).getString(path), args);
    }

    public String getName() {
        return name;
    }

    public static Set<String> getNames() {
        return cache.keySet();
    }

    public static void reload(String name) {
        Class<? extends MultiLanguageRegistry> c = null;
        try {
            c = (Class<? extends MultiLanguageRegistry>) Class.forName(cache.get(name));
            c.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
