
package dev.salmon.weatherchanger;

import dev.salmon.weatherchanger.command.WeatherChangerCommand;
import dev.salmon.weatherchanger.config.WeatherConfig;
import dev.salmon.weatherchanger.listener.WeatherListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = WeatherChanger.ID, name = WeatherChanger.NAME, version = WeatherChanger.VER)
public class WeatherChanger {
    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    @Mod.Instance
    private static WeatherChanger instance;
    private WeatherConfig config;

    @Mod.EventHandler
    protected void init(FMLInitializationEvent event) {
        this.config = new WeatherConfig();
        this.config.preload();
        new WeatherChangerCommand().register();
        registerListeners(new WeatherListener());
    }

    public WeatherConfig getConfig() { return this.config; }

    /* Used to register forge event listeners */
    public static void registerListeners(Object... objects) {
        for (Object o : objects) {
            MinecraftForge.EVENT_BUS.register(o);
        }
    }

    public static WeatherChanger getWeatherChanger() {
        return instance;
    }
}
