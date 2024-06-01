package us.smartmc.smartbot.message;

import me.imsergioh.pluginsapi.language.DefaultLanguageMessage;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;

@LangMessagesInfo(name = "discordbot/main")
public enum MainMessages implements IMessageCategory {

    @DefaultLanguageMessage("&a¡You linked your discord account correctly!")
    LINKED_DISCORD_CORRECTLY,

    @DefaultLanguageMessage("&aThank you, &e{0}&a! You linked your discord account successfully! ")
    LINKED_DISCORD_SUCCESSFULLY;

    @Override
    public String getFieldName() {
        return name();
    }
}
