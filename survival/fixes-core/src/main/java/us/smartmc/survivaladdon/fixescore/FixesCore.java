package us.smartmc.survivaladdon.fixescore;

import lombok.Getter;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;
import us.smartmc.survivaladdon.fixescore.command.ExecuteAtBungeeCommand;

@AddonInfo(name = "fixes-core")
public class FixesCore extends AddonPlugin {

    @Getter
    private static FixesCore plugin;

    @Override
    public void start() {
        plugin = this;
        RedisConnection.mainConnection = new RedisConnection("localhost", 6379);
        registerCommand(new ExecuteAtBungeeCommand());
    }

    @Override
    public void stop() {

    }
}
