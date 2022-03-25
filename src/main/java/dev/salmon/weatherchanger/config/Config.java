package dev.salmon.weatherchanger.config;

import dev.salmon.weatherchanger.WeatherChanger;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.io.File;

public class Config extends Vigilant {

    public Config() {
        super(new File("./config", WeatherChanger.ID + ".toml"), WeatherChanger.NAME);
        initialize();
    }

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
            type = PropertyType.DECIMAL_SLIDER,
            name = "Intensity",
            description = "Allows you to control the intensity and repetition of Thunder.",
            category = "General",
            minF = 0f,
            maxF = 1.0f
    )
    private float thunderStrength = 1.0f;

    public WeatherType getCurrentWeather() {
        return WeatherType.from(this.currentWeather);
    }

    public float getStrength() {
        return this.strength;
    }

    public float getThunderStrength() {
        return this.thunderStrength;
    }

}