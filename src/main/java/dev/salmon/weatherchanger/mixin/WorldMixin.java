package dev.salmon.weatherchanger.mixin;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.config.WeatherType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(Side.CLIENT)
@Mixin(World.class)
public class WorldMixin {

    @Inject(method = "getThunderStrength", at = @At("RETURN"), cancellable = true)
    public void setThunderStrength(CallbackInfoReturnable<Float> clr) {
        if (WeatherChanger.Instance.getConfig().getCurrentWeather() == WeatherType.STORM) {
            clr.setReturnValue(WeatherChanger.Instance.getConfig().getThunderStrength());
        }
    }

    @Inject(method = "getRainStrength", at = @At("RETURN"), cancellable = true)
    public void setPrecipitationStrength(CallbackInfoReturnable<Float> clr) {
        switch (WeatherChanger.Instance.getConfig().getCurrentWeather()) {
            case VANILLA:
                clr.setReturnValue(clr.getReturnValue());
                break;
            case CLEAR:
                clr.setReturnValue(0.0F);
                break;
            case SNOW:
            case RAIN:
            case STORM:
                clr.setReturnValue(WeatherChanger.Instance.getConfig().getStrength());
                break;
        }
    }

}
