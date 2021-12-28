package dev.salmon.weatherchanger;

import dev.salmon.weatherchanger.command.WeatherChangerCommand;
import dev.salmon.weatherchanger.config.WeatherChangerConfig;
import dev.salmon.weatherchanger.updater.Updater;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Mod(modid = WeatherChanger.ID, name = WeatherChanger.NAME, version = WeatherChanger.VER)
public class WeatherChanger {
    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    public static File jarFile;
    public static File modDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), NAME);
    public static WeatherChangerConfig config;

    @Mod.EventHandler
    protected void onFMLPreInitialization(FMLPreInitializationEvent event) {
        if (!modDir.exists()) modDir.mkdirs();
        jarFile = event.getSourceFile();
    }

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        new WeatherChangerCommand().register();
        config = new WeatherChangerConfig();
        config.preload();
        Updater.update();
    }
}
