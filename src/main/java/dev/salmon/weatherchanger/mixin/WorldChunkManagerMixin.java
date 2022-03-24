package dev.salmon.weatherchanger.mixin;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.config.WeatherType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(Side.CLIENT)
@Mixin(WorldChunkManager.class)
public class WorldChunkManagerMixin {

    @Inject(method = "getTemperatureAtHeight", at=@At("RETURN"), cancellable = true)
    private void setSnowing(CallbackInfoReturnable<Float> clr) {
        if (WeatherChanger.Instance.getConfig().getCurrentWeather() == WeatherType.SNOW) {
            clr.setReturnValue(0.10F);
        }
    }

}
