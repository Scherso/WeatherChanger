package dev.salmon.weatherchanger.handler;

import dev.salmon.weatherchanger.config.WeatherType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;

import java.util.Random;

public abstract class WeatherHandler extends IRenderHandler {

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final Random random = new Random();
    protected int rendererUpdateCount;

    public abstract WeatherType getType();
    public abstract void update();
    public void initialize(WorldClient world) {
    }

}
