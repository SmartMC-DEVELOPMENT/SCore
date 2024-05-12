package us.smartmc.game.luckytowers.messages;

import me.imsergioh.pluginsapi.language.DefaultLanguageItem;
import me.imsergioh.pluginsapi.language.DefaultLanguageMessage;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;

@LangMessagesInfo(name = "luckytowers/admin")
public enum AdminMessages implements IMessageCategory {

    @DefaultLanguageMessage("&cUnknown map to configure! Make sure to set the mapId correctly!")
    editor_unknownMap,

    @DefaultLanguageMessage("&aAdded spawn correctly")
    editor_spawnAdded,

    @DefaultLanguageMessage("&cRemoved last spawn correctly")
    editor_spawnRemovedLast,

    @DefaultLanguageMessage("&aSpawn has been set for the map!")
    editor_spawnSet,

    @DefaultLanguageMessage("&aCorner {0} has been set!")
    editor_cornerSet,

    @DefaultLanguageMessage("&fMaintenance mode has been set to {0}&f!")
    editor_maintenanceSet,

    @DefaultLanguageMessage("&aMap has been saved correctly!")
    editor_regionSaved,

    @DefaultLanguageMessage("&cError while trying to save region map! Reason: {0}")
    editor_regionSaveError;

    @Override
    public String getFieldName() {
        return name();
    }
}
