package dev.salmon.weatherchanger.handler.weather;

import dev.salmon.weatherchanger.config.WeatherType;
import dev.salmon.weatherchanger.handler.WeatherHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;

public class ClearHandler extends WeatherHandler {
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
    }

    public void update() {
    }

    public WeatherType getType() {
        return WeatherType.CLEAR;
    }
}
