package dev.salmon.weatherchanger.util;

import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

import java.util.Arrays;

/**
 * @author Deftu
 * @version 1.0.0
 */
public class ForgeHelper {

    public static void registerListeners(Object... objects) {
        for (Object o : objects) {
            MinecraftForge.EVENT_BUS.register(o);
        }
    }

    public static void unregisterListeners(Object... objects) {
        for (Object o : objects) {
            MinecraftForge.EVENT_BUS.unregister(o);
        }
    }

    public static void registerCommands(ICommand... command) {
        Arrays.stream(command).forEachOrdered(ClientCommandHandler.instance::registerCommand);
    }

}