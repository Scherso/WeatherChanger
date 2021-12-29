package dev.salmon.weatherchanger.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Multithread {
    public static final Locale LOCALE = getLocale();
    private static final ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("WeatherChanger-%d").build());

    private static Locale getLocale() {
        return new Locale(System.getProperty("user.language"), System.getProperty("user.country"));
    }

    public static void async(Runnable runnable) {
        executorService.submit(runnable);
    }

    public static Gson getGson() {
        return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setDateFormat(getDate().toPattern()).create();
    }

    public static SimpleDateFormat getDate() {
        return new SimpleDateFormat("EEEEE dd MMMMM yyyy", LOCALE);
    }
}
