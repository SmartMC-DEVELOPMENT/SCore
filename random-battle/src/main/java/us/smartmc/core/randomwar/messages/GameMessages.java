package us.smartmc.core.randomwar.messages;

import me.imsergioh.pluginsapi.language.DefaultLanguageMessage;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;


@LangMessagesInfo(name = "random-battle/game")
public enum GameMessages implements IMessageCategory {

    @DefaultLanguageMessage("&cYou don't have permissions to execute this command")
    cmd_adminGame_noPermission,
    @DefaultLanguageMessage("&aLobby has been set correctly!")
    cmd_adminGame_lobbySet;


    @Override
    public String getFieldName() {
        return name();
    }
}
