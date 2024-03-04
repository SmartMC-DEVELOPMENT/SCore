package us.smartmc.core.instance.player;

import org.bukkit.Sound;

public enum PlayerCurrencyCoin {

    SMARTCOINS("coins", "coin", Sound.LEVEL_UP, 1F, 10F),
    ENIGMA_BOXES("enigmaboxes", "enigmabox", Sound.ENDERDRAGON_HIT),
    GEMS("gems", "gem", Sound.ORB_PICKUP, 1F, 0.2F);


    final String documentKey, singleUnitName;
    float soundPitch, soundVolume = 1;
    Sound sound;

    PlayerCurrencyCoin(String documentKey, String singleUnitName) {
        this.documentKey = documentKey;
        this.singleUnitName = singleUnitName;
    }

    PlayerCurrencyCoin(String documentKey, String singleUnitName, Sound sound) {
        this(documentKey, singleUnitName);
        this.sound = sound;
    }

    PlayerCurrencyCoin(String documentKey, String singleUnitName, Sound sound, float volume, float pitch) {
        this(documentKey, singleUnitName, sound);
        this.soundVolume = volume;
        this.soundPitch = pitch;
    }

    public String getSingleUnitAddedMessagePath() {
        return singleUnitName + "_added";
    }

    public String getAddedMessagePath() {
        return documentKey + "_added";
    }

}
