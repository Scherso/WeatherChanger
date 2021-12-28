package dev.salmon.weatherchanger.config;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.updater.DownloadGui;
import dev.salmon.weatherchanger.updater.Updater;
import gg.essential.api.EssentialAPI;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.io.File;

public class WeatherChangerConfig extends Vigilant {
    @Property(
            type = PropertyType.SELECTOR,
            name = "Pick the Weather",
            description = "Control the Weather!",
            category = "General",
            options = {"Clear", "Snow", "Rain"}
    )
    public static int currentWeather = 0;
    // 0 == Clear, 1 == Snow, 2 == Rain

    @Property(
            type = PropertyType.DECIMAL_SLIDER,
            name = "Intensity",
            description = "Allows you to control the intensity or opacity of the weather Particles.",
            category = "General",
            minF = 0f,
            maxF = 1.0f
    )
    public static float strength = 1f;
    // We'll get this working later

    // don't worry about what's down here
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

    public WeatherChangerConfig() {
        super(new File(WeatherChanger.modDir, WeatherChanger.ID + ".toml"), "Weather Changer");
        initialize();
    }
}
