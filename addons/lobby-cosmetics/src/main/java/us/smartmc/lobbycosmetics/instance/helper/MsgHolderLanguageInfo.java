package us.smartmc.lobbycosmetics.instance.helper;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import org.bson.Document;

import java.util.ArrayList;
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
        return holder.getString(id + ".name");
    }

    public List<String> getDescription() {
        List<String> list = new ArrayList<>(holder.get(id, Document.class).getList("description", String.class));
        list.replaceAll(s -> "&7" + s);
        return list;
    }

}
