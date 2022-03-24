package dev.salmon.weatherchanger.config;

public enum WeatherType {

    VANILLA(0),
    CLEAR(1),
    SNOW(2),
    RAIN(3),
    STORM(4);

    private final int id;
    WeatherType(int id) {
        this.id = id;
    }

    public static WeatherType from(int id) {
        for (WeatherType value : values()) {
            if(value.id == id) {
                return value;
            }
        }

        return VANILLA;
    }

}
