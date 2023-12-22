package us.smartmc.core.pluginsapi.spigot.event;

import us.smartmc.core.pluginsapi.connection.RedisConnection;
import us.smartmc.core.pluginsapi.language.Language;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;

public class PlayerLanguageChangedEvent extends CorePlayerEvent {

    private final Language previousLanguage;
    private final Language newLanguage;

    public PlayerLanguageChangedEvent(CorePlayer corePlayer,
                                      Language previousLanguage, Language newLanguage) {
        super(corePlayer);
        this.previousLanguage = previousLanguage;
        this.newLanguage = newLanguage;
        RedisConnection.mainConnection.getResource().publish("lang:change",
                corePlayer.get().getUniqueId().toString() + " " + newLanguage.name());
    }

    public Language getPreviousLanguage() {
        return previousLanguage;
    }

    public Language getNewLanguage() {
        return newLanguage;
    }
}
