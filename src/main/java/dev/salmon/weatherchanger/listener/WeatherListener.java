package dev.salmon.weatherchanger.listener;

import dev.salmon.weatherchanger.WeatherChanger;
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

public class WeatherListener {

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            WorldClient world = Minecraft.getMinecraft().theWorld;
            WeatherChanger weatherChanger = WeatherChanger.getInstance();
            WeatherType currentWeather = weatherChanger.getConfig().getCurrentWeather();
            IRenderHandler currentHandler = world.provider.getWeatherRenderer();
            WeatherHandler handler = weatherChanger.getHandlerRegistry().from(currentWeather);

            if (currentHandler != handler) {
                world.provider.setWeatherRenderer(handler instanceof VanillaHandler ? null : handler);
                handler.initialize(world);
            }

            if (currentHandler instanceof WeatherHandler && !Minecraft.getMinecraft().isGamePaused()) {
                ((WeatherHandler) currentHandler).update();
                World serverWorld = MinecraftServer.getServer().worldServers[0];
                WorldInfo worldInfo = serverWorld.getWorldInfo();
                if (worldInfo.isRaining()) {
                    worldInfo.setCleanWeatherTime(Integer.MAX_VALUE);
                    worldInfo.setRainTime(0);
                    worldInfo.setThunderTime(0);
                    worldInfo.setRaining(false);
                    worldInfo.setThundering(false);
                }
            }
        }
    }

}