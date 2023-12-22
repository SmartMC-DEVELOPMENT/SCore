package us.smartmc.snowgames.object;

import org.bukkit.Bukkit;

import java.util.Timer;
import java.util.TimerTask;

public abstract class PluginGameEvent implements PluginGameEventImplementation {

    protected final EventType type;

    public PluginGameEvent(EventType type) {
        this.type = type;
        onStart();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                onEnd();
            }
        }, 1000 * 10);
    }

    @Override
    public void onStart() {
        Bukkit.getOnlinePlayers().forEach(this::updatePlayer);
    }

    @Override
    public EventType getType() {
        return type;
    }
}
