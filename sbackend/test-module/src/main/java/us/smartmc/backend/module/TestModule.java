package us.smartmc.backend.module;

public class TestModule extends ModulePlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("LOADED MODULE TEST");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.exit(0);
    }
}
