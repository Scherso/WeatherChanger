package dev.salmon.weatherchanger.handler;

import dev.salmon.weatherchanger.config.WeatherType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WeatherHandlerRegistry {

    private final Map<WeatherType, WeatherHandler> handlers = new HashMap<>();

    public void addHandler(WeatherHandler handler) {
        handlers.putIfAbsent(handler.getType(), handler);
    }

    public Map<WeatherType, WeatherHandler> getHandlers() {
        return Collections.unmodifiableMap(handlers);
    }

    public WeatherHandler from(WeatherType type) {
        return handlers.get(type);
    }

}