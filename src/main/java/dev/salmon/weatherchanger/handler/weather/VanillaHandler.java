package dev.salmon.weatherchanger.handler.weather;

import dev.salmon.weatherchanger.config.WeatherType;
import dev.salmon.weatherchanger.handler.WeatherHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

import java.util.Random;

public class VanillaHandler extends WeatherHandler {
    private static final Random RANDOM = new Random();

    public void initialize(WorldClient world) {
        int i = (300 + RANDOM.nextInt(600)) * 20;
        Minecraft.getMinecraft().theWorld.getWorldInfo().setCleanWeatherTime(i);
    }

    public void update() {
    }

    public void render(float partialTicks, WorldClient world, Minecraft mc) {
    }

    public WeatherType getType() {
        return WeatherType.VANILLA;
    }
}