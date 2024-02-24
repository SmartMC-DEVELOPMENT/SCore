package us.smartmc.lobbycosmetics.message;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

@LangMessagesInfo(name = CosmeticsInfoMessages.NAME)
public class CosmeticsInfoMessages extends MultiLanguageRegistry {

    public static final String NAME = "cosmetics_info/lobby";

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        registerSection("hats", "Hats", Arrays.asList("This is default description for cosmetic section", "It's very crazy guuuuys", "Not configurated yet!!"));
        registerSection("pets", "Pets", Arrays.asList("This is another default descr. for cosmetic section", "Les gooooo", "Make the admin configure or fix server lol"));

        registerMenu("main", "Cosmetics Main Menu");

        holder.save();
    }

    public void registerMenu(String id, String title) {
        registerConfigValues("menu_" + id + ".", new Document()
                .append("title", title));
    }

    public void registerSection(String id, String name, List<String> description) {
        registerConfigValues("section_" + id + ".", new Document()
                .append("name", name)
                .append("description", description));
    }

    private void registerConfigValues(String pathPrefix, Document document) {
        String holderName = getName();
        for (Language language : Language.values()) {
            LanguageMessagesHolder holder = LanguagesHandler.get(language).get(holderName);
            boolean hadChanges = false;
            for (String key : document.keySet()) {
                String finalkey = pathPrefix + key;
                if (!hadChanges && holder.containsKey(finalkey)) hadChanges = true;
                holder.registerDefault(finalkey, document.get(key));
            }
            if (hadChanges) holder.save();
        }
    }

}
