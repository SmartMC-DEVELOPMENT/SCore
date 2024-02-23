package us.smartmc.lobbycosmetics.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;

public class OpenCosmeticSectionAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        String id = args[0];
        switch (id) {
            case "hats": {
                handler.player().sendMessage("Hats example! YIIIII");
                return;
            }
            case "pets": {
                handler.player().sendMessage("Pets example! MIAAAU");
                return;
            }
        }
    }
}
