
package dev.salmon.weatherchanger;

import dev.salmon.weatherchanger.command.WeatherChangerCommand;
import dev.salmon.weatherchanger.config.WeatherConfig;
import dev.salmon.weatherchanger.listener.WeatherListener;
import gg.essential.vigilance.Vigilance;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;

@Mod(modid = WeatherChanger.ID, name = WeatherChanger.NAME, version = WeatherChanger.VER)
public class WeatherChanger {
    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    private static WeatherChanger instance;
    private WeatherConfig config;

    @Mod.EventHandler
    protected void preInit(FMLPreInitializationEvent event) {
        instance = this;
    }

    @Mod.EventHandler
    protected void init(FMLInitializationEvent event) {
        Vigilance.initialize();
        this.config = new WeatherConfig();
        this.config.preload();
        this.registerCommands(new WeatherChangerCommand());
        registerListeners(new WeatherListener());
    }

    public WeatherConfig getConfig() { return this.config; }

    /* Used to register forge event listeners */
    public static void registerListeners(Object... objects) {
        for (Object o : objects) {
            MinecraftForge.EVENT_BUS.register(o);
        }
    }

    /* Used to unregister forge event listeners */
    public static void unregisterListeners(Object... objects) {
        for (Object o : objects) {
            MinecraftForge.EVENT_BUS.unregister(o);
        }
    }

    /* Used to register Forge commands. Should only be used on startup */
    private void registerCommands(ICommand... command) {
        Arrays.stream(command).forEachOrdered(ClientCommandHandler.instance::registerCommand);
    }

    public static WeatherChanger getWeatherChanger() { return instance; }
}
