package us.smartmc.game.luckytowers.messages;

import me.imsergioh.pluginsapi.language.*;

@LangMessagesInfo(name = "luckytowers/game")
public enum GameMessages implements IMessageCategory {

    @DefaultLanguageMessagesList({"&7Teleport to {0}"})
    itemDescription_teleportToPlayer,

    @DefaultLanguageMessage("Vote")
    menu_vote_title,

    @DefaultLanguageMessage("Navigator")
    menu_navigator_title,

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
    session_message_started,

    @DefaultLanguageMessage("&6+{0} coins!")
    player_addedCoins,

    @DefaultLanguageMessage("&b+{0} kill!")
    player_addedKill,

    @DefaultLanguageMessage("&c&lSTART HAS BEEN CANCELLED!")
    session_cancelled,

    @DefaultLanguageMessage("<color:#B7FFFF>¡CUIDADO TE PUEDES CAER!")
    title_dontFall,

    @DefaultLanguageMessage("<green>Iniciando partida")
    forceStarting,

    @DefaultLanguageMessage("&a&lEmpezando en")
    starting_title,
    @DefaultLanguageMessage("&c&l{0}")
    starting_subtitle,

    @DefaultLanguageMessage("&c&lYOU DIED!")
    death_title,

    @DefaultLanguageMessage("&fNext game you will do better! :D")
    death_subtitle,

    @DefaultLanguageMessage("&a&lYOU WON!")
    win_title,

    @DefaultLanguageMessage("&fCongratulations! You did a very good job! :D")
    win_subtitle,

    @DefaultLanguageMessage("&6+{0} coins! ({1})")
    addedCoins_withReason,

    @DefaultLanguageMessage("&6+{0} coins!")
    addedCoins_withoutReason,

    @DefaultLanguageMessage("Kill")
    coinsReason_kill,

    @DefaultLanguageMessage("Win")
    coinsReason_win;


    @Override
    public String getFieldName() {
        return name();
    }
}
