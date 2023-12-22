package us.smartmc.core.pluginsapi.language;

import java.io.File;
import java.util.HashMap;

public class LanguageHolder {

    private static final String FOLDER_PATH = "/home/lang";
    private final File dir;
    private final Language language;

    private final HashMap<String, LanguageMessagesHolder> messagesHolders = new HashMap<>();

    public LanguageHolder(Language language) {
        this.language = language;
        this.dir = new File(FOLDER_PATH + "/" + language.name().toUpperCase());
        if (!dir.exists()) dir.mkdirs();
    }

    public LanguageMessagesHolder register(String name) {
        LanguageMessagesHolder holder = new LanguageMessagesHolder(name, this);
        messagesHolders.put(name, holder);
        return holder;
    }

    public LanguageMessagesHolder get(String name) {
        if (!messagesHolders.containsKey(name)) return register(name);
        return messagesHolders.get(name);
    }

    public File getDir() {
        return dir;
    }

    public Language getLanguage() {
        return language;
    }
}
