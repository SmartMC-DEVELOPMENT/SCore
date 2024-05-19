package us.smartmc.lobbycosmetics.instance.helper;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.util.LineLimiter;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

@Getter
public class MsgHolderLanguageInfo {

    private final String id;
    private final Language language;

    @Getter
    private final LanguageMessagesHolder holder;

    public MsgHolderLanguageInfo(String id, Language language, String holderName) {
        this.id = id;
        this.language = language;
        this.holder = LanguagesHandler.get(language).get(holderName);
    }

    public String getName() {
        String name = holder.getString(id + ".name");
        if (name == null) return "&4No name found!";
        return "&a" + name;
    }

    public List<String> getDescription() {
        String description = holder.get(id, Document.class).getString("description");
        List<String> list;
        list = LineLimiter.limitLines(Arrays.asList(description), 32);
        list.replaceAll(s -> "&7" + s);
        return list;
    }

}
