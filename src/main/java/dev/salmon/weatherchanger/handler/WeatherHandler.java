package dev.salmon.weatherchanger.handler;

import net.minecraftforge.client.IRenderHandler;

public abstract class WeatherHandler extends IRenderHandler {
    protected int rendererUpdateCount;
    public abstract void update();
}
