package dev.salmon.weatherchanger.config;

public enum WeatherType {

    VANILLA(0),
    CLEAR(1),
    SNOW(2),
    RAIN(3),
    STORM(4),
    REAL(5); // future update stuff

    private final int raw;
    WeatherType(int raw) {
        this.raw = raw;
    }

    public int getRaw() {
        return raw;
    }

    public static WeatherType from(int raw) {
        for (WeatherType value : values()) {
            if (value.raw == raw) {
                return value;
            }
        }

        return REAL;
    }

}