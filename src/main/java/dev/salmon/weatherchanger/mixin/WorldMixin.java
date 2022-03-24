package dev.salmon.weatherchanger.mixin;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.config.Config;
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
public abstract class WorldMixin {

    Config config = new Config();

    @Inject(method = "getThunderStrength", at = @At("RETURN"), cancellable = true)
    public void setThunderStrength(CallbackInfoReturnable<Float> clr) {
        clr.setReturnValue(WeatherChanger.Instance.getConfig().getThunderStrength());
    }

    @Inject(method = "getRainStrength", at = @At("RETURN"), cancellable = true)
    public void setPrecipitationStrength(CallbackInfoReturnable<Float> clr) {
        if (config.getCurrentWeather() == WeatherType.CLEAR || config.getCurrentWeather() == WeatherType.VANILLA) return;

        if (config.getCurrentWeather() == WeatherType.RAIN || config.getCurrentWeather() == WeatherType.STORM || config.getCurrentWeather() == WeatherType.SNOW) {
            clr.setReturnValue(WeatherChanger.Instance.getConfig().getStrength());
        }
    }

}
