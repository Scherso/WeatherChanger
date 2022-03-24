package dev.salmon.weatherchanger.command;

import dev.salmon.weatherchanger.WeatherChanger;
import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.DefaultHandler;

public class Command extends gg.essential.api.commands.Command {

    public Command() {
        super(WeatherChanger.ID, true);
    }

    @DefaultHandler
    public void Handle() {
        EssentialAPI.getGuiUtil().openScreen(WeatherChanger.Instance.getConfig().gui());
    }

}