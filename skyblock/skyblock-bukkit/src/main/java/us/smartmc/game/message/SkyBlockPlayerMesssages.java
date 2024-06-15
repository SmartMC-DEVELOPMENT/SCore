package us.smartmc.game.message;

import me.imsergioh.pluginsapi.language.DefaultLanguageMessage;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;

@LangMessagesInfo(name = "skyblock/players_main")
public enum SkyBlockPlayerMesssages implements IMessageCategory {

    @DefaultLanguageMessage("&6+{0} coins!")
    coinsAdded;

    @Override
    public String getFieldName() {
        return name();
    }
}
