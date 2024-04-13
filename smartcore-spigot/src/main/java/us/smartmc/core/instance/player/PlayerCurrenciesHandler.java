package us.smartmc.core.instance.player;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.player.CorePlayerData;
import org.bson.Document;
import org.bukkit.Sound;
import us.smartmc.core.exception.CorePluginException;
import us.smartmc.core.messages.GeneralMessages;

import java.util.HashMap;
import java.util.Map;

public class PlayerCurrenciesHandler {

    private final SmartCorePlayer corePlayer;

    public PlayerCurrenciesHandler(SmartCorePlayer corePlayer) {
        this.corePlayer = corePlayer;
    }

    public void add(IPlayerCurrencyCoin coin, long amount, String reason) {
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
        if (coin.getSound() == null) return;
        corePlayer.playSound(coin.getSound(), coin.getSoundVolume(), coin.getSoundPitch());
    }

    public void remove(IPlayerCurrencyCoin coin, long amount, String reason) throws CorePluginException {
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
            if (coin.getSound() == null) return;
            corePlayer.playSound(coin.getSound(), coin.getSoundVolume(), coin.getSoundPitch());
        } else {
            throw new CorePluginException("remove " + coin.getName() + " method tried to remove more than player have!");
        }
    }

    public void set(IPlayerCurrencyCoin coin, long amount) {
        getData().getDocument().put(coin.getDocumentKey(), amount);
    }

    public long get(IPlayerCurrencyCoin coin) {
        Document document = getData().getDocument();
        if (!document.containsKey(coin.getDocumentKey())) {
            document.put(coin.getDocumentKey(), 0);
        }
        return document.get(coin.getDocumentKey(), Number.class).longValue();
    }

    private CorePlayerData getData() {
        return corePlayer.getPlayerData();
    }

}
