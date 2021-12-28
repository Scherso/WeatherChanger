package dev.salmon.weatherchanger.listener;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.handler.ClearHandler;
import dev.salmon.weatherchanger.handler.RainHandler;
import dev.salmon.weatherchanger.handler.SnowHandler;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WeatherListener {
    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent event) {
        int currentWeather = WeatherChanger.getWeatherChanger().getConfig().getCurrentWeather();
        IRenderHandler currentWeatherHandler = event.world.provider.getWeatherRenderer();

        if (currentWeather == 0 && !(currentWeatherHandler instanceof ClearHandler)) {
            event.world.provider.setWeatherRenderer(new ClearHandler());
        } else if (currentWeather == 1 && !(currentWeatherHandler instanceof RainHandler)) {
            event.world.provider.setWeatherRenderer(new RainHandler());
        } else if (currentWeather == 2 && !(currentWeatherHandler instanceof SnowHandler)) {
            event.world.provider.setWeatherRenderer(new SnowHandler());
        }
    }
}
