package us.smartmc.game.luckytowers.messages;

import me.imsergioh.pluginsapi.language.DefaultLanguageItem;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;

@LangMessagesInfo(name = "luckytowers/admin_items")
public enum AdminItems implements IMessageCategory {

    @DefaultLanguageItem(
            material = "END_PORTAL_FRAME",
            name = "&bSet main map spawn",
            description = {"&7Set the lobby/spawn of the map"}
    )
    editorMode_item_setSpawn,

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
    editorMode_item_setCorners,

    @DefaultLanguageItem(
            material = "BEDROCK",
            name = "&bToggle maintenance",
            description = {"&7Enable or disable the maintenance", "for the map"}
    )
    editorMode_item_toggleMaintenance;

    @Override
    public String getFieldName() {
        return name();
    }
}
