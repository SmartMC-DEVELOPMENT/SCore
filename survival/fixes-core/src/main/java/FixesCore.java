import lombok.Getter;
import us.smartmc.smartaddons.plugin.AddonPlugin;

public class FixesCore extends AddonPlugin {

    @Getter
    private static FixesCore plugin;

    @Override
    public void start() {
        plugin = this;
    }

    @Override
    public void stop() {

    }
}
