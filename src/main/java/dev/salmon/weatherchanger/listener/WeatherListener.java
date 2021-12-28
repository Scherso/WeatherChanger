package dev.salmon.weatherchanger.listener;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.handler.ClearHandler;
import dev.salmon.weatherchanger.handler.RainHandler;
import dev.salmon.weatherchanger.handler.SnowHandler;
import dev.salmon.weatherchanger.handler.WeatherHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WeatherListener {
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            WorldClient world = Minecraft.getMinecraft().theWorld;
            int currentWeather = WeatherChanger.getWeatherChanger().getConfig().getCurrentWeather();
            IRenderHandler currentWeatherHandler = world.provider.getWeatherRenderer();

            if (currentWeather == 0 && currentWeatherHandler instanceof WeatherHandler) {
              world.provider.setWeatherRenderer(null);
            } else if (currentWeather == 1 && !(currentWeatherHandler instanceof ClearHandler)) {
                world.provider.setWeatherRenderer(new ClearHandler());
            } else if (currentWeather == 2 && !(currentWeatherHandler instanceof SnowHandler)) {
                world.provider.setWeatherRenderer(new SnowHandler());
            } else if (currentWeather == 3 && !(currentWeatherHandler instanceof RainHandler)) {
                world.provider.setWeatherRenderer(new RainHandler());
            }

            if (currentWeatherHandler instanceof WeatherHandler && !Minecraft.getMinecraft().isGamePaused()) {
                ((WeatherHandler) currentWeatherHandler).update();
            }
        }
    }
}
