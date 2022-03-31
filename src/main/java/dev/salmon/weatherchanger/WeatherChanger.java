package dev.salmon.weatherchanger;

//#if MC==10809
import cc.woverflow.onecore.utils.Updater;
//#endif
import dev.salmon.weatherchanger.command.Command;
import dev.salmon.weatherchanger.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = WeatherChanger.ID, name = WeatherChanger.NAME, version = WeatherChanger.VER)
public class WeatherChanger {

    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    private Config config;

    @Mod.Instance(ID)
    public static WeatherChanger Instance;

    //#if MC==10809
    @Mod.EventHandler
    protected void onPreInit(FMLPreInitializationEvent event) {
        Updater.INSTANCE.addToUpdater(event.getSourceFile(), NAME, ID, VER, "W-OVERFLOW/WeatherChanger");
    }
    //#endif

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
