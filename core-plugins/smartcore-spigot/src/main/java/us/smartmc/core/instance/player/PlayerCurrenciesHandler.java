package us.smartmc.core.instance.player;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.player.CorePlayerData;
import org.bukkit.Sound;
import us.smartmc.core.exception.CorePluginException;
import us.smartmc.core.messages.GeneralMessages;

import java.util.HashMap;
import java.util.Map;

public class PlayerCurrenciesHandler {

    private final Map<PlayerCurrencyCoin, Long> amounts = new HashMap<>();

    private final SmartCorePlayer corePlayer;

    public PlayerCurrenciesHandler(SmartCorePlayer corePlayer) {
        this.corePlayer = corePlayer;
    }

    public void add(PlayerCurrencyCoin coin, long amount, String reason) {
        set(coin, get(coin) + amount);
        String msgPath = amount == 1 ? coin.getSingleUnitAddedMessagePath() : coin.getAddedMessagePath();
        if (reason != null) {
            reason = "(" + reason + ")";
        } else {
            reason = "";
        }
        String message = LanguagesHandler.get(corePlayer.getLanguage()).get(GeneralMessages.NAME).getString(msgPath);
        if (message != null)
            corePlayer.sendLanguageMessage(GeneralMessages.NAME, msgPath, amount, reason);
        if (coin.sound == null) return;
        corePlayer.playSound(coin.sound, coin.soundVolume, coin.soundPitch);
    }

    public void remove(PlayerCurrencyCoin coin, long amount, String reason) throws CorePluginException {
        long currencyAmount = get(coin);
        long withAmountRemoved = currencyAmount - amount;
        if (withAmountRemoved >= 0) {
            set(coin, withAmountRemoved);
            String msgPath = amount == 1 ? coin.getSingleUnitAddedMessagePath() : coin.getAddedMessagePath();
            if (reason != null) {
                reason = "(" + reason + ")";
            } else {
                reason = "";
            }
            String message = LanguagesHandler.get(corePlayer.getLanguage()).get(GeneralMessages.NAME).getString(msgPath);
            if (message != null)
                corePlayer.sendLanguageMessage(GeneralMessages.NAME, msgPath, amount, reason);
            if (coin.sound == null) return;
            corePlayer.playSound(coin.sound, coin.soundVolume, coin.soundPitch);
        } else {
            throw new CorePluginException("remove " + coin.name() + " method tried to remove more than player have!");
        }
    }

    public void set(PlayerCurrencyCoin coin, long amount) {
        getData().getDocument().put(coin.documentKey, amount);
    }

    public long get(PlayerCurrencyCoin coin) {
        if (!amounts.containsKey(coin)) {
            long amount = getData().getDocument().containsKey(coin.documentKey) ?
                    getData().getDocument().get(coin.documentKey, Long.class) : 0;
            amounts.put(coin, amount);
        }
        return amounts.get(coin);
    }

    private CorePlayerData getData() {
        return corePlayer.getPlayerData();
    }

}
