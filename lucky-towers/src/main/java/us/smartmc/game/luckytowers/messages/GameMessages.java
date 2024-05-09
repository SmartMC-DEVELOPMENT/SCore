package us.smartmc.game.luckytowers.messages;

import me.imsergioh.pluginsapi.language.*;

@LangMessagesInfo(name = "luckytowers/game")
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
            material = "DIAMOND_SWORD",
            name = "&bPlay",
            description = {"&7Menu for playing the different modes", "&7or something like this"}
    )
    lobbyHotbar_item_play,

    @DefaultLanguageItem(
            material = "CLOCK",
            name = "&bOptions",
            description = {"&7Configure your options", "&7to your tastes"}
    )
    lobbyHotbar_item_options,

    @DefaultLanguageMessage("&aStarting game in &e{0}&as...")
    session_actionBar_startingIn,

    @DefaultLanguageMessage("&c&l¡FIGHT!")
    session_message_started;


    @Override
    public String getFieldName() {
        return name();
    }
}
