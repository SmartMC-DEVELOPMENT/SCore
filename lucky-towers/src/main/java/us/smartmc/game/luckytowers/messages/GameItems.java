package us.smartmc.game.luckytowers.messages;

import me.imsergioh.pluginsapi.language.DefaultLanguageItem;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;

@LangMessagesInfo(name = "luckytowers/game_items")
public enum GameItems implements IMessageCategory {

    @DefaultLanguageItem(
            material = "CHEST",
            name = "&bVote",
            description = {"&7Vote aspects of the game"}
    )
    waiting_vote,

    @DefaultLanguageItem(
            material = "COMPASS",
            name = "&aPlayer navegator",
            description = {"&7Navigate through all the players alive", "&7and teleport to them to spectate"}
    )
    spectator_compass,

    @DefaultLanguageItem(
            material = "BLUE_BED",
            name = "&cLeave",
            description = {"&7Go back to the lobby"}
    )
    spectator_goBack,

    @DefaultLanguageItem(
            material = "BEDROCK",
            name = "&cThis feature is upcoming",
            description = {"&7In a few days this feature is going to work.", "&7We are working and developing this!"}
    )
    upcoming_feature,

    @DefaultLanguageItem(
            material = "BEDROCK",
            name = "&a{0}",
            description = {"&7They are <mapPlaying.{0}> playing", "&r", "&aClick to join!"}
    )
    menu_games_playMapTemplate;

    @Override
    public String getFieldName() {
        return name();
    }
}
