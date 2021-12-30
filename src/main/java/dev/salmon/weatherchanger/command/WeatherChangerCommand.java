
package dev.salmon.weatherchanger.command;

import dev.salmon.weatherchanger.WeatherChanger;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;
import gg.essential.api.utils.GuiUtil;

public class WeatherChangerCommand extends Command {

    public WeatherChangerCommand() {
        super(WeatherChanger.ID);
    }

    @DefaultHandler
    public void handle() {
        GuiUtil.open(WeatherChanger.getWeatherChanger().getConfig().gui());
    }

}