package dev.salmon.weatherchanger.updater;

import com.google.gson.JsonObject;
import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.config.WeatherConfig;
import gg.essential.api.EssentialAPI;
import gg.essential.api.utils.Multithreading;
import gg.essential.api.utils.WebUtil;
import gg.essential.universal.UDesktop;
import kotlin.Unit;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class Updater {
    public static String updateUrl = "";
    public static String latestTag;
    public static boolean shouldUpdate = false;

    public static void update() {
        Multithreading.runAsync(() -> {
            try {
                JsonObject latestRelease = WebUtil.fetchJSON("https://api.github.com/repos/W-OVERFLOW/" + WeatherChanger.ID + "/releases/latest").getObject();
                latestTag = latestRelease.get("tag_name").getAsString();
                DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(StringUtils.substringBefore(WeatherChanger.VER, "-"));
                DefaultArtifactVersion latestVersion = new DefaultArtifactVersion(StringUtils.substringBefore(StringUtils.substringAfter(latestTag, "v"), "-"));
                if (currentVersion.compareTo(latestVersion) >= 0) {
                    return;
                }
                updateUrl = latestRelease.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
                if (!updateUrl.isEmpty()) {
                    if (WeatherConfig.showUpdate) {
                        EssentialAPI.getNotifications().push(WeatherChanger.NAME, WeatherChanger.NAME + " has a new update (" + latestTag + ")! Click here to download it automatically!", Updater::openUpdateGui);
                    }
                    shouldUpdate = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static Unit openUpdateGui() {
        EssentialAPI.getGuiUtil().openScreen(new DownloadGui());
        return Unit.INSTANCE;
    }

    public static boolean download(String url, File file) {
        if (file.exists()) return true;
        url = url.replace(" ", "%20");
        try {
            WebUtil.downloadToFile(url, file, WeatherChanger.NAME + "/" + WeatherChanger.VER);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return file.exists();
    }

    /**
     * Adapted from RequisiteLaunchwrapper under LGPLv3
     * https://github.com/Qalcyo/RequisiteLaunchwrapper/blob/main/LICENSE
     */
    public static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Opening Deleter task...");
            try {
                String runtime = getJavaRuntime();
                if (Minecraft.isRunningOnMac) {
                    UDesktop.open(WeatherChanger.jarFile.getParentFile());
                }
                File file = new File(WeatherChanger.modDir.getParentFile(), "Deleter-1.2.jar");
                Runtime.getRuntime()
                        .exec("\"" + runtime + "\" -jar \"" + file.getAbsolutePath() + "\" \"" + WeatherChanger.jarFile.getAbsolutePath() + "\"");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.currentThread().interrupt();
        }));
    }

    /**
     * Gets the current Java runtime being used.
     *
     * @link https://stackoverflow.com/a/47925649
     */
    public static String getJavaRuntime() throws IOException {
        String os = System.getProperty("os.name");
        String java = System.getProperty("java.home") + File.separator + "bin" + File.separator +
                (os != null && os.toLowerCase(Locale.ENGLISH).startsWith("windows") ? "java.exe" : "java");
        if (!new File(java).isFile()) {
            throw new IOException("Unable to find suitable java runtime at " + java);
        }
        return java;
    }
}