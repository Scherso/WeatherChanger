
package dev.salmon.weatherchanger;

import dev.salmon.weatherchanger.command.WeatherChangerCommand;
import dev.salmon.weatherchanger.config.WeatherConfig;
import dev.salmon.weatherchanger.listener.WeatherListener;
import dev.salmon.weatherchanger.util.ForgeHelper;
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
    @Mod.Instance private static WeatherChanger instance;
    private WeatherConfig config;

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        Vigilance.initialize();
        config = new WeatherConfig();
        config.preload();
        ForgeHelper.registerCommands(new WeatherChangerCommand());
        ForgeHelper.registerListeners(new WeatherListener());
    }

    public WeatherConfig getConfig() {
        return config;
    }

    public static WeatherChanger getInstance() { return instance; }

}
