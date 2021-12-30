package dev.salmon.weatherchanger.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Multithreading {

    private static Locale CACHED_LOCALE;
    private static Gson CACHED_GSON;
    private static final ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("WeatherChanger-%d").build());

    private static Locale getLocale() {
        return CACHED_LOCALE == null ? CACHED_LOCALE = new Locale(
                System.getProperty("user.language"),
                System.getProperty("user.country")) :
                CACHED_LOCALE;
    }

    public static Gson getGson() {
        return CACHED_GSON == null ? CACHED_GSON = new GsonBuilder()
                .setDateFormat(getFormattedDate().toPattern())
                .setPrettyPrinting()
                .create() : CACHED_GSON;
    }

    public static void runAsync(Runnable runnable) {
        executorService.submit(runnable);
    }

    public static SimpleDateFormat getFormattedDate() {
        return new SimpleDateFormat("EEEEE dd MMMMM yyyy", CACHED_LOCALE);
    }

}
