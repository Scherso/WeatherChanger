package dev.salmon.weatherchanger.handler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.IRenderHandler;

import java.util.Random;

public abstract class WeatherHandler extends IRenderHandler {
    protected Minecraft mc = Minecraft.getMinecraft();
    protected final Random random = new Random();
    protected int rendererUpdateCount;

    public abstract void update();
}
