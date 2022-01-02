package dev.salmon.weatherchanger.config;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.updater.DownloadGui;
import dev.salmon.weatherchanger.updater.Updater;
import gg.essential.api.EssentialAPI;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.io.File;

public class WeatherConfig extends Vigilant {

    /**
     * Values:
     *      0 - Vanilla
     *      1 - Clear
     *      2 - Snow
     *      3 - Rain
     *      4 - Storm
     *      5 - Real
     */
    @Property(
            type = PropertyType.SELECTOR,
            name = "Pick the Weather",
            description = "Control the Weather!",
            category = "General",
            options = {
                    "Vanilla",
                    "Clear",
                    "Snow",
                    "Rain",
                    "Storm"
            }
    )
    public static int currentWeather = 0;

    @Property(
            type = PropertyType.DECIMAL_SLIDER,
            name = "Intensity",
            description = "Allows you to control the intensity or opacity of the weather Particles.",
            category = "General",
            maxF = 1.0f
    )
    public static float strength = 1.0f;

    @Property(
            type = PropertyType.SWITCH,
            name = "Show Update Notification",
            description = "Show a notification when you start Minecraft informing you of new updates.",
            category = "Updater"
    )
    public static boolean showUpdate = true;

    @Property(
            type = PropertyType.BUTTON,
            name = "Update Now",
            description = "Update by clicking the button.",
            category = "Updater"
    )
    public void update() {
        if (Updater.shouldUpdate) EssentialAPI.getGuiUtil()
                .openScreen(new DownloadGui());
        else EssentialAPI.getNotifications()
                .push(WeatherChanger.NAME, "No update had been detected at startup, and thus the update GUI has not been shown.");
    }

    public WeatherConfig() {
        super(new File(WeatherChanger.modDir, WeatherChanger.ID + ".toml"), WeatherChanger.NAME);
        initialize();
    }

    public WeatherType getCurrentWeather() {
        return WeatherType.from(currentWeather);
    }
}
