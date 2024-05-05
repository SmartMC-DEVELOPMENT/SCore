package us.smartmc.game.luckytowers.messages;

import me.imsergioh.pluginsapi.language.*;

@LangMessagesInfo(name = "random-battle/game")
public enum GameMessages implements IMessageCategory {

    @DefaultLanguageMessage("&cYou don't have permissions to execute this command")
    cmd_adminGame_noPermission,
    @DefaultLanguageMessage("&aLobby has been set correctly!")
    cmd_adminGame_lobbySet,
    @DefaultLanguageMessage("Games")
    menu_games_title,
    @DefaultLanguageMessage("Options")
    menu_options_title,

    @DefaultLanguageMessage("&fAdmin mode:&a Enabled")
    editorMode_enabled,
    @DefaultLanguageMessage("&fAdmin mode:&c Disabled")
    editorMode_disabled,

    @DefaultLanguageItem(
            material = "BEACON",
            name = "&bSelect teams",
            description = {"&7Select the team you want to edit in editor mode"}
    )
    editorMode_item_selectorTeam,

    @DefaultLanguageItem(
            material = "DIAMOND_BLOCK",
            name = "&bAdd spawn",
            description = {"&7Add spawn to the current team selected"}
    )
    editorMode_item_addSpawn;


    @Override
    public String getFieldName() {
        return name();
    }
}
