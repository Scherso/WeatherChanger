
package dev.salmon.weatherchanger.command;

import dev.salmon.weatherchanger.WeatherChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WeatherChangerCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return WeatherChanger.ID;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Opens GUI for WeatherChanger";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    /* Minecraft will close the GuiScreen instantly if you do not display it 1 tick after the command is sent */
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        WeatherChanger.registerListeners(this);
    }

    @SubscribeEvent
    public void openGui(TickEvent.ClientTickEvent event) {
        Minecraft.getMinecraft().displayGuiScreen(WeatherChanger.getInstance().getConfig().gui());
        WeatherChanger.unregisterListeners(this);
    }
}