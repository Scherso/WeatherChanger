
package dev.salmon.weatherchanger.command;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.util.ForgeHelper;
import dev.salmon.weatherchanger.util.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.TimeUnit;

public class WeatherChangerCommand extends CommandBase {
    public String getCommandName() {
        return WeatherChanger.ID;
    }

    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    public void processCommand(ICommandSender sender, String[] args) {
        Multithreading.schedule(() -> Minecraft.getMinecraft().displayGuiScreen(WeatherChanger.getInstance().getConfig().gui()), 100, TimeUnit.MILLISECONDS);
    }
}