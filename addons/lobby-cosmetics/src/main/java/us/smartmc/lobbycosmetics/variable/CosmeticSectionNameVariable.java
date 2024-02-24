package us.smartmc.lobbycosmetics.variable;

import me.imsergioh.pluginsapi.instance.IVariableListener;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.smartmc.lobbycosmetics.instance.helper.MsgHolderLanguageInfo;
import us.smartmc.lobbycosmetics.message.CosmeticsMainMessages;

public class CosmeticSectionNameVariable implements IVariableListener<Player> {

    private static final String PREFIX = "<cosmetic_section_name_";

    @Override
    public String parse(String message) {
        return parse(null, message);
    }

    @Override
    public String parse(Player player, String message) {
        if (!message.startsWith(PREFIX)) return message;
        String sectionName = getSection(message);
        Language language = Language.getDefault();
        if (player != null) language = PlayerLanguages.get(player.getUniqueId());
        return get(language, sectionName);
    }

    public String get(Language language, String sectionName) {
        MsgHolderLanguageInfo info = new MsgHolderLanguageInfo("section_" + sectionName, language, CosmeticsMainMessages.NAME);
        return ChatColor.stripColor(info.getName().replace("&", "§"));
    }

    public String getSection(String message) {
        return message.replaceFirst(PREFIX, "").replace(">", "");
    }

}
