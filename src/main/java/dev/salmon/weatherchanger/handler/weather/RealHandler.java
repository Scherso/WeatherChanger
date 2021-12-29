package dev.salmon.weatherchanger.handler.weather;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.config.WeatherConfig;
import dev.salmon.weatherchanger.handler.WeatherHandler;
import dev.salmon.weatherchanger.util.Multithread;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class RealHandler extends WeatherHandler {
    private long lastWeatherCheck;
    private final JsonParser jsonParser = new JsonParser();

    @Override
    public void update() {
        this.rendererUpdateCount++;

        /* Checks local weather every minute */
        if (System.currentTimeMillis() - this.lastWeatherCheck > 60 * 1000) {
            this.lastWeatherCheck = System.currentTimeMillis();

            Multithread.async(()-> {
                WeatherConfig config = WeatherChanger.getWeatherChanger().getConfig();
                String apiKey = config.getWeatherApiKey();

                if (apiKey.isEmpty()) {
                    this.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "You do not have an OpenWeatherMap API key set!"));
                    return;
                }

                String country = config.getWeatherCountry().toUpperCase();
                String state = config.getWeatherState().toUpperCase();
                String city = config.getWeatherCity().toUpperCase();
                /* State is only needed if the country is USA */
                String location = country.equals("USA") ? String.format("%s,%s,%s", country, state, city) : String.format("%s,%s", country, city);

                String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", location, apiKey);

                System.out.println(url);

                JsonObject response = new JsonObject();
                try (CloseableHttpClient client = HttpClients.createDefault()) {
                    HttpGet request = new HttpGet(url);

                    try {
                        StringWriter writer = new StringWriter();
                        IOUtils.copy(new InputStreamReader(client.execute(request).getEntity().getContent(), StandardCharsets.UTF_8), writer);

                        response = jsonParser.parse(writer.toString()).getAsJsonObject();
                    } catch (JsonSyntaxException ex) {
                        /* Retrieved bad Json */
                        ex.printStackTrace();
                        return;
                    }

                    System.out.println(response);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {

    }
}
