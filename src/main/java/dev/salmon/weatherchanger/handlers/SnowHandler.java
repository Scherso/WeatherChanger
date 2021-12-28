package dev.salmon.weatherchanger.handlers;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.config.WeatherChangerConfig;
import net.minecraftforge.client.IRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class SnowHandler extends IRenderHandler {

    private final Random random = new Random();
    private final ResourceLocation locationSnowPng = new ResourceLocation("textures/environment/snow.png");
    public int rendererUpdateCount; // i think i need this to make the render actually render
    private float[] snowX = new float[1024];
    private float[] snowY = new float[1024];

    public SnowHandler() {
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f = (float) (j - 16);
                float f1 = (float) (i - 16);
                float f2 = MathHelper.sqrt_float(f * f + f1 * f1);
                this.snowX[i << 5 | j] = -f1 / f2;
                this.snowY[i << 5 | j] = f / f2;
            }
        }
    }

    // do i need to give credit to minecraft for stealing their code?
    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {

            EntityRenderer renderer = mc.entityRenderer;
            renderer.enableLightmap();
            Entity entity = mc.getRenderViewEntity();
            int i = MathHelper.floor_double(entity.posX);
            int j = MathHelper.floor_double(entity.posY);
            int k = MathHelper.floor_double(entity.posZ);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.disableCull();
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.alphaFunc(516, 0.1F);
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
            int l = MathHelper.floor_double(d1);
            int i1 = 5;

            if (mc.gameSettings.fancyGraphics) {
                i1 = 10;
            }

            int j1 = -1;
            float f1 = (float) this.rendererUpdateCount + partialTicks;
            worldrenderer.setTranslation(-d0, -d1, -d2);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k1 = k - i1; k1 <= k + i1; ++k1) {
                for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                    int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                    double d3 = (double) this.snowX[i2] * 0.5D;
                    double d4 = (double) this.snowY[i2] * 0.5D;
                    blockpos$mutableblockpos.set(l1, 0, k1);

                    int j2 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                    int k2 = j - i1;
                    int l2 = j + i1;

                    if (k2 < j2) {
                        k2 = j2;
                    }

                    if (l2 < j2) {
                        l2 = j2;
                    }

                    int i3 = j2;

                    if (j2 < l) {
                        i3 = l;
                    }

                    if (k2 != l2) {
                        this.random.setSeed((long) (l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761));
                        blockpos$mutableblockpos.set(l1, k2, k1);

                        if (j1 != 1) {

                            j1 = 1;
                            mc.getTextureManager().bindTexture(this.locationSnowPng);
                            worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        double d8 = (double) (((float) (this.rendererUpdateCount & 511) + partialTicks) / 512.0F);
                        double d9 = this.random.nextDouble() + (double) f1 * 0.01D * (double) ((float) this.random.nextGaussian());
                        double d10 = this.random.nextDouble() + (double) (f1 * (float) this.random.nextGaussian()) * 0.001D;
                        double d11 = (double) ((float) l1 + 0.5F) - entity.posX;
                        double d12 = (double) ((float) k1 + 0.5F) - entity.posZ;
                        float f6 = MathHelper.sqrt_double(d11 * d11 + d12 * d12) / (float) i1;
                        float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * WeatherChanger.getWeatherChanger().getConfig().getStrength();
                        blockpos$mutableblockpos.set(l1, i3, k1);
                        int i4 = (world.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
                        int j4 = i4 >> 16 & 65535;
                        int k4 = i4 & 65535;
                        worldrenderer.pos((double) l1 - d3 + 0.5D, (double) k2, (double) k1 - d4 + 0.5D).tex(0.0D + d9, (double) k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                        worldrenderer.pos((double) l1 + d3 + 0.5D, (double) k2, (double) k1 + d4 + 0.5D).tex(1.0D + d9, (double) k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                        worldrenderer.pos((double) l1 + d3 + 0.5D, (double) l2, (double) k1 + d4 + 0.5D).tex(1.0D + d9, (double) l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                        worldrenderer.pos((double) l1 - d3 + 0.5D, (double) l2, (double) k1 - d4 + 0.5D).tex(0.0D + d9, (double) l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f5).lightmap(j4, k4).endVertex();
                    }
                }
            }

            if (j1 >= 0) {
                tessellator.draw();
            }

            worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            renderer.disableLightmap();
    }
}