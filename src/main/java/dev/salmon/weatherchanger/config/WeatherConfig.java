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

    @Property(
            type = PropertyType.SELECTOR,
            name = "Pick the Weather",
            description = "Control the Weather!",
            category = "General",
            options = {"Vanilla", "Clear", "Snow", "Rain", "Storm", "Hail", "Fog", "Cloudy", "Real"}
    )
    private int currentWeather = 0;
    // 0 == Vanilla, 1 == Clear, 2 == Snow, 3 == Rain, 4 == Storm, 5 == Hail, 6 == Fog, 7 == Cloudy, 8 == Real

    @Property(
            type = PropertyType.DECIMAL_SLIDER,
            name = "Intensity",
            description = "Allows you to control the intensity or opacity of the weather Particles.",
            category = "General",
            minF = 0f,
            maxF = 1.0f
    )
    public static float strength = 1.0f;

    @Property(
            type = PropertyType.SWITCH,
            name = "Real Life Weather",
            description = "Toggle weather that corresponds your local weather.",
            category = "General",
            subcategory = "Real Weather"
    )
    public static boolean realWeather = false;

    @Property(
            type = PropertyType.TEXT,
            name = "OpenWeatherMap API Key",
            description = "Used to gather local weather data if using the \"Real WeatherChanger\"",
            category = "RealWeather",
            placeholder = "None",
            protectedText = true
    )
    private String weatherApiKey = "";

    @Property(
            type = PropertyType.TEXT,
            name = "Country",
            description = "Desired Country to grab weather data from.",
            category = "RealWeather",
            placeholder = "None",
            protectedText = true
    )
    private String weatherCountry = "";

    @Property(
            type = PropertyType.TEXT,
            name = "State",
            description = "Desired USA State to grab weather data from.\n(Only works if Country is set to USA)\n(Cannot contain spaces)",
            category = "RealWeather",
            placeholder = "None",
            protectedText = true
    )
    private String weatherState = "";

    @Property(
            type = PropertyType.TEXT,
            name = "City",
            description = "Desired City to grab weather data from.\n(Cannot contain spaces)",
            category = "RealWeather",
            placeholder = "None",
            protectedText = true
    )
    private String weatherCity = "";

    @Property(
            type = PropertyType.SWITCH,
            name = "Show Update Notification",
            description = "Show a notification when you start Minecraft informing you of new updates.",
            category = "Updater"
    )
    private boolean showUpdate = true;

    public int getCurrentWeather() { return currentWeather; }

    public float getStrength() { return this.strength; }

    public String getWeatherApiKey() { return this.weatherApiKey; }

    public String getWeatherCountry() { return this.weatherCountry; }

    public String getWeatherState() { return this.weatherState; }

    public String getWeatherCity() { return this.weatherCity; }

    public boolean isShowUpdate() { return this.showUpdate; }
}
