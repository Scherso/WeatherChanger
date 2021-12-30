package dev.salmon.weatherchanger.listener;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.config.WeatherConfig;
import dev.salmon.weatherchanger.config.WeatherType;
import dev.salmon.weatherchanger.handler.*;
import dev.salmon.weatherchanger.handler.weather.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class WeatherListener {

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            WorldClient world = Minecraft.getMinecraft().theWorld;
            WeatherChanger weatherChanger = WeatherChanger.getInstance();
            WeatherType currentWeather = WeatherChanger.getInstance().getConfig().getCurrentWeather();
            IRenderHandler currentWeatherHandler = world.provider.getWeatherRenderer();

            if (currentWeather == 0 && currentWeatherHandler instanceof WeatherHandler) {
                world.provider.setWeatherRenderer(null);
                /* Reset worldInfo cleanWeather timer */
                int i = (300 + (new Random()).nextInt(600)) * 20;
                Minecraft.getMinecraft().theWorld.getWorldInfo().setCleanWeatherTime(i);
            } else if (currentWeather == 1 && !(currentWeatherHandler instanceof ClearHandler)) {
                world.provider.setWeatherRenderer(new ClearHandler());
            } else if (currentWeather == 2 && !(currentWeatherHandler instanceof SnowHandler)) {
                world.provider.setWeatherRenderer(new SnowHandler());
            } else if (currentWeather == 3 && !(currentWeatherHandler instanceof RainHandler)) {
                world.provider.setWeatherRenderer(new RainHandler(weatherChanger, true));
            } else if (WeatherConfig.realWeather && !(currentWeatherHandler instanceof RealHandler)) {
                world.provider.setWeatherRenderer(new RealHandler());
            } else if (currentWeather == 6 && !(currentWeatherHandler instanceof HailHandler)) {
                world.provider.setWeatherRenderer(new HailHandler(true));
            }

            if (currentWeatherHandler instanceof WeatherHandler && !Minecraft.getMinecraft().isGamePaused()) {
                ((WeatherHandler) currentWeatherHandler).update();

                World serverWorld = MinecraftServer.getServer().worldServers[0];
                WorldInfo worldInfo = serverWorld.getWorldInfo();
                if (worldInfo.isRaining()) {
                    worldInfo.setCleanWeatherTime(1000000000);
                    worldInfo.setRainTime(0);
                    worldInfo.setThunderTime(0);
                    worldInfo.setRaining(false);
                    worldInfo.setThundering(false);
                }
            }
        }
    }

}