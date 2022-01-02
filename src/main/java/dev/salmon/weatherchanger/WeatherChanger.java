
package dev.salmon.weatherchanger;

import dev.salmon.weatherchanger.command.WeatherChangerCommand;
import dev.salmon.weatherchanger.config.WeatherConfig;
import dev.salmon.weatherchanger.handler.WeatherHandlerRegistry;
import dev.salmon.weatherchanger.handler.weather.*;
import dev.salmon.weatherchanger.listener.WeatherListener;
import dev.salmon.weatherchanger.updater.Updater;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Mod(modid = WeatherChanger.ID, name = WeatherChanger.NAME, version = WeatherChanger.VER)
public class WeatherChanger {

    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    @Mod.Instance private static WeatherChanger instance;
    private WeatherConfig config;
    private WeatherHandlerRegistry handlerRegistry;
    public static File jarFile;
    public static File modDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), NAME);

    @Mod.EventHandler
    protected void onFMLPreInitialization(FMLPreInitializationEvent event) {
        if (!modDir.exists()) modDir.mkdirs();
        jarFile = event.getSourceFile();
    }

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        config = new WeatherConfig();
        config.preload();

        handlerRegistry = new WeatherHandlerRegistry();
        handlerRegistry.addHandler(new ClearHandler());
        handlerRegistry.addHandler(new RainHandler());
        // TODO - handlerRegistry.addHandler(new RealHandler());
        handlerRegistry.addHandler(new SnowHandler());
        handlerRegistry.addHandler(new StormHandler());
        handlerRegistry.addHandler(new VanillaHandler());

        new WeatherChangerCommand().register();
        MinecraftForge.EVENT_BUS.register(new WeatherListener());
        Updater.update();
    }

    public WeatherConfig getConfig() {
        return config;
    }

    public WeatherHandlerRegistry getHandlerRegistry() {
        return handlerRegistry;
    }

    public static WeatherChanger getInstance() { return instance; }

}
