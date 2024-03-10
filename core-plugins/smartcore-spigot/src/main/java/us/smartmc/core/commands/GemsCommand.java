package us.smartmc.core.commands;

import us.smartmc.core.instance.player.PlayerCurrencyCoin;

public class GemsCommand extends CurrencyCommand {

    public GemsCommand() {
        super("gems", PlayerCurrencyCoin.GEMS);
    }
}
