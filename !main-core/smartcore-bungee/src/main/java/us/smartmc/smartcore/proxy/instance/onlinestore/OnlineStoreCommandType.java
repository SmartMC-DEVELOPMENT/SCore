package us.smartmc.smartcore.proxy.instance.onlinestore;

import lombok.Getter;

@Getter
public enum OnlineStoreCommandType {

    UNKNOWN(null),
    PURCHASE("purchase_commands"),
    REMOVE("remove_commands"),
    REFUND("refund_commands"),
    CHARGEBACK("chargeback_commands"),
    RENEW("renew_commands");


    final String commandSectionName;

    OnlineStoreCommandType(String commandSectionName) {
        this.commandSectionName = commandSectionName;
    }

}
