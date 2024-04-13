package us.smartmc.core.instance.player;

import org.bukkit.Sound;

public interface IPlayerCurrencyCoin {

    String getDocumentKey();
    String getSingleUnitName();
    float getSoundPitch();
    float getSoundVolume();
    Sound getSound();
    String getSingleUnitAddedMessagePath();
    String getAddedMessagePath();
    String getName();

}
