package us.smartmc.core.commands;

import us.smartmc.core.instance.player.PlayerCurrencyCoin;

public class CoinsCommand extends CurrencyCommand {

    public CoinsCommand() {
        super("coins", PlayerCurrencyCoin.SMARTCOINS);
    }
}
