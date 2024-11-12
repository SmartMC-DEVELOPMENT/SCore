package us.smartmc.gamescore.manager;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Getter
public class TimeWorldManager {

    private final String name;
    private final int time;

    public TimeWorldManager(String name, int time) {
        this.name = name;
        this.time = time;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                World world = getWorld(name);
                if (world == null) {
                    timer.cancel();
                    return;
                }
                world.setTime(time);
            }
        }, 10, 3000);
    }

    public World getWorld(String name) {
        return Bukkit.getWorld(name);
    }

    public static TimeWorldManager create(World world, int time) {
        return new TimeWorldManager(world.getName(), time);
    }

}
