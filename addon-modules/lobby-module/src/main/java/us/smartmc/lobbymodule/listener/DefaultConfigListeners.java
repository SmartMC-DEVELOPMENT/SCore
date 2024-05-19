package us.smartmc.lobbymodule.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import us.smartmc.lobbymodule.config.LobbyConfig;
import us.smartmc.smartaddons.plugin.AddonListener;

public class DefaultConfigListeners extends AddonListener implements Listener {

    private final LobbyConfig config;

    public DefaultConfigListeners(LobbyConfig config) {
        this.config = config;
    }

    @EventHandler
    public void cancelClick(InventoryClickEvent event) {
        if (!isEnabled()) return;
        event.setCancelled(config.getBoolean("cancel_click_event"));
    }

    @EventHandler
    public void sunnyWeather(WeatherChangeEvent event) {
        if (!isEnabled()) return;
        if (!config.getBoolean("always_weather_clear")) return;
        event.setCancelled(event.toWeatherState());
    }

}
