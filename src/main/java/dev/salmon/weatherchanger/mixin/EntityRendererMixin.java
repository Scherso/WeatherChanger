package dev.salmon.weatherchanger.mixin;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.config.WeatherType;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Inject(method = "addRainParticles", at = @At("HEAD"), cancellable = true)
    private void cancelRainParticles(CallbackInfo ci) {
        if (WeatherChanger.Instance.getConfig().getCurrentWeather() == WeatherType.SNOW) {
            ci.cancel();
        }
    }

}
