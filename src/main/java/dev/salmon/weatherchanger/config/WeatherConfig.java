package dev.salmon.weatherchanger.config;

import dev.salmon.weatherchanger.WeatherChanger;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.io.File;

public class WeatherConfig extends Vigilant {

    public WeatherConfig() {
        super(new File("./config", WeatherChanger.ID + ".toml"), WeatherChanger.NAME);
    }

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
    private int currentWeather = 0;

    @Property(
            type = PropertyType.DECIMAL_SLIDER,
            name = "Intensity",
            description = "Allows you to control the intensity or opacity of the weather Particles.",
            category = "General",
            minF = 0f,
            maxF = 1.0f
    )
    private float strength = 1.0f;

    @Property(
            type = PropertyType.SWITCH,
            name = "Show Update Notification",
            description = "Show a notification when you start Minecraft informing you of new updates.",
            category = "Updater"
    )
    private boolean showUpdate = true;

    public WeatherType getCurrentWeather() {
        return WeatherType.from(currentWeather);
    }

    public float getStrength() {
        return strength;
    }

    public boolean isShowUpdate() {
        return showUpdate;
    }
}
