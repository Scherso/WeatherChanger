package dev.salmon.weatherchanger;

import dev.salmon.weatherchanger.command.Command;
import dev.salmon.weatherchanger.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = WeatherChanger.ID, name = WeatherChanger.NAME, version = WeatherChanger.VER, acceptedMinecraftVersions = "1.8.9, 1.12.2")
public class WeatherChanger {

    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    private Config config;

    @Mod.Instance(ID)
    public static WeatherChanger Instance;

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        new Command().register();
        config = new Config();
        config.preload();
    }

    public Config getConfig() {
        return this.config;
    }

}
