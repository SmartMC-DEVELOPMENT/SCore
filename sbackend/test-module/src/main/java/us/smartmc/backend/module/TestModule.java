package us.smartmc.backend.module;

@ModulePluginInfo(name = "TestModule", version = "DEV")
public class TestModule extends ModulePlugin {

    public TestModule() {
        super();
    }

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
