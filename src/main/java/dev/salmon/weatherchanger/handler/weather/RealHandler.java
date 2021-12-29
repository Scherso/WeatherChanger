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
    private WeatherHandler weatherRender;

    @Override
    public void update() {
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

                    int weatherId = response.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsInt();
                    /* only reason this should return null is if OpenWeatherMap changes their ID's */
                    WeatherType weatherType = WeatherType.fromId(weatherId);
                    System.out.println(weatherType.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

//            if (!this.mc.isGamePaused())
//                this.weatherRender.update();
        }
    }

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
//        this.weatherRender.render(partialTicks, world, mc);
    }

    public enum WeatherType {
        THUNDERSTORM_WITH_LIGHT_RAIN(200, "Thunderstorm"),
        THUNDERSTORM_WITH_RAIN(201, "Thunderstorm"),
        THUNDERSTORM_WITH_HEAVY_RAIN(202, "Thunderstorm"),
        LIGHT_THUNDERSTORM(210, "Thunderstorm"),
        THUNDERSTORM(211, "Thunderstorm"),
        HEAVY_THUNDERSTORM(212, "Thunderstorm"),
        RAGGED_THUNDERSTORM(221, "Thunderstorm"),
        THUNDERSTORM_WITH_LIGHT_DRIZZLE(230, "Thunderstorm"),
        THUNDERSTORM_WITH_DRIZZLE(231, "Thunderstorm"),
        THUNDERSTORM_WITH_HEAVY_DRIZZLE(232, "Thunderstorm"),

        LIGHT_INTENSITY_DRIZZLE(300, "Drizzle"),
        DRIZZLE(301, "Drizzle"),
        HEAVY_INTENSITY_DRIZZLE(302, "Drizzle"),
        LIGHT_INTENSITY_DRIZZLE_RAIN(310, "Drizzle"),
        DRIZZLE_RAIN(311, "Drizzle"),
        HEAVY_INTENSITY_DRIZZLE_RAIN(312, "Drizze"),
        SHOWER_RAIN_AND_DRIZZLE(313, "Drizzle"),
        HEAVY_SHOWER_RAIN_AND_DRIZZLE(314, "Drizzle"),
        SHOWER_DRIZZLE(321, "Drizzle"),

        LIGHT_RAIN(500, "Rain"),
        MODERATE_RAIN(501, "Rain"),
        HEAVY_INTENSITY_RAIN(502, "Rain"),
        VERY_HEAVY_RAIN(503, "Rain"),
        EXTREME_RAIN(504, "Rain"),
        FREEZING_RAIN(511, "Rain"),
        LIGHT_INTENSITY_SHOWER_RAIN(520, "Rain"),
        SHOWER_RAIN(521, "Rain"),
        HEAVY_INTENSITY_SHOWER_RAIN(522, "Rain"),
        RAGGED_SHOWER_RAIN(531, "Rain"),

        LIGHT_SNOW(600, "Snow"),
        SNOW(601, "Snow"),
        HEAVY_SNOW(602, "Snow"),
        SLEET(611, "Snow"),
        LIGHT_SHOWER_SLEET(612, "Snow"),
        SHOWER_SLEET(613, "Snow"),
        LIGHT_RAIN_AND_SNOW(615, "Snow"),
        RAIN_AND_SNOW(616, "Snow"),
        LIGHT_SHOWER_SNOW(620, "Snow"),
        SHOWER_SNOW(621, "Snow"),
        HEAVY_SHOWER_SNOW(622, "Snow"),

        MIST(701, "Mist"),
        SMOKE(711, "Smoke"),
        HAZE(721, "Haze"),
        SAND_DUST_WHIRLS(731, "Dust"),
        FOG(741, "Fog"),
        SAND(751, "Sand"),
        DUST(761, "Dust"),
        VOLCANIC_ASH(762, "Ash"),
        SQUALLS(771, "Squall"),
        TORNADO(781, "Tornado"),

        CLEAR_SKY(800, "Clear"),

        FEW_CLOUDS(801, "Clouds"),
        SCATTERED_CLOUDS(802, "Clouds"),
        BROKEN_CLOUDS(803, "Clouds"),
        OVERCAST_CLOUDS(804, "Clouds");

        int id;
        String category;

        WeatherType(int id, String category) {
            this.id = id;
            this.category = category;
        }

        public static WeatherType fromId(int id) {
            for (WeatherType type : values()) {
                if (type.id == id)
                    return type;
            }

            return null;
        }

        public int getId() { return this.id; }

        public String getCategory() { return this.category; }

        public String getName() { return this.name().replace("_", " ").toLowerCase(); }

        @Override
        public String toString() { return this.getName(); }
    }
}
