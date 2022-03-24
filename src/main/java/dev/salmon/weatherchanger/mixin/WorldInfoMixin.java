package dev.salmon.weatherchanger.mixin;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.config.WeatherType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(Side.CLIENT)
@Mixin(WorldInfo.class)
public abstract class WorldInfoMixin {


    @Inject(method = "isRaining", at = @At("RETURN"), cancellable = true)
    private void setPrecipitation(CallbackInfoReturnable<Boolean> clr) {

        switch (WeatherChanger.Instance.getConfig().getCurrentWeather()) {
            case VANILLA:
                break;
            case CLEAR:
                clr.setReturnValue(false);
                break;
            case SNOW:
            case RAIN:
            case STORM:
                clr.setReturnValue(true);
                break;
        }
    }

    @Inject(method = "isThundering", at = @At("RETURN"), cancellable = true)
    private void setThundering(CallbackInfoReturnable<Boolean> clr) {
        if (WeatherChanger.Instance.getConfig().getCurrentWeather() == WeatherType.STORM) {
            clr.setReturnValue(true);
        }
    }

}
