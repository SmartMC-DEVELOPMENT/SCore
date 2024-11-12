package us.smartmc.gamescore.listener;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import us.smartmc.gamescore.manager.WeatherWorldManager;

public class WeatherWorldListeners implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        World world = event.getWorld();
        if (!WeatherWorldManager.isActive(world)) return;
        event.setCancelled(true);
    }
}
