package dev.salmon.weatherchanger.command;

import dev.salmon.weatherchanger.WeatherChanger;
import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class WeatherChangerCommand extends Command {
    public WeatherChangerCommand() {
        super(WeatherChanger.ID, true);
    }

    @DefaultHandler
    public void handle() {
        EssentialAPI.getGuiUtil().openScreen(WeatherChanger.config.gui());
    }
}