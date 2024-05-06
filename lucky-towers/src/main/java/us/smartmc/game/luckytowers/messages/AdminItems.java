package us.smartmc.game.luckytowers.messages;

import me.imsergioh.pluginsapi.language.DefaultLanguageItem;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;

@LangMessagesInfo(name = "luckytowers/admin_items")
public enum AdminItems implements IMessageCategory {

    @DefaultLanguageItem(
            material = "BEACON",
            name = "&bSelect teams",
            description = {"&7Select the team you want to edit in editor mode"}
    )
    editorMode_item_selectorTeam,

    @DefaultLanguageItem(
            material = "DIAMOND_BLOCK",
            name = "&bAdd spawn",
            description = {"&7Add spawn to the current map"}
    )
    editorMode_item_addSpawn,

    @DefaultLanguageItem(
            material = "REDSTONE_BLOCK",
            name = "&cRemove last spawn",
            description = {"&7Remvoe last spawn from the spawnlist of map"}
    )
    editorMode_item_removeLastSpawn,

    @DefaultLanguageItem(
            material = "DIAMOND_AXE",
            name = "&aSet corners",
            description = {"&7Right-Click to set Pos1", "&7Left-Click to set Pos2"}
    )
    editorMode_item_setCorners;

    @Override
    public String getFieldName() {
        return name();
    }
}
