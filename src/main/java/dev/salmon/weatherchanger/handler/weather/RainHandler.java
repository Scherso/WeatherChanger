package dev.salmon.weatherchanger.handler.weather;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.handler.WeatherHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import org.lwjgl.opengl.GL11;

public class RainHandler extends WeatherHandler {

    private final WeatherChanger weatherChanger;
    private boolean cloudy;

    private final ResourceLocation locationRainPng = new ResourceLocation("textures/environment/rain.png");
    private float[] rainXCoords = new float[1024];
    private float[] rainYCoords = new float[1024];
    private int rainSoundCounter;

    public RainHandler(WeatherChanger weatherChanger, boolean cloudy) {
        this.weatherChanger = weatherChanger;
        this.cloudy = cloudy;
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f = (float)(j - 16);
                float f1 = (float)(i - 16);
                float f2 = MathHelper.sqrt_float(f * f + f1 * f1);
                this.rainXCoords[i << 5 | j] = -f1 / f2;
                this.rainYCoords[i << 5 | j] = f / f2;
            }
        }
    }

    private void addRainParticles() {
        float f = weatherChanger.getConfig().getStrength();

        if (!this.mc.gameSettings.fancyGraphics) {
            f /= 2.0F;
        }

        if (f != 0.0F) {
            this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
            Entity entity = this.mc.getRenderViewEntity();
            World world = this.mc.theWorld;
            BlockPos blockpos = new BlockPos(entity);
            int i = 10;
            double d0 = 0.0D;
            double d1 = 0.0D;
            double d2 = 0.0D;
            int j = 0;
            int k = (int)(100.0F * f * f);

            if (this.mc.gameSettings.particleSetting == 1) {
                k >>= 1;
            } else if (this.mc.gameSettings.particleSetting == 2) {
                k = 0;
            }

            for (int l = 0; l < k; ++l) {
                BlockPos blockpos1 = world.getPrecipitationHeight(blockpos.add(this.random.nextInt(i) - this.random.nextInt(i), 0, this.random.nextInt(i) - this.random.nextInt(i)));
                BiomeGenBase biomegenbase = world.getBiomeGenForCoords(blockpos1);
                BlockPos blockpos2 = blockpos1.down();
                Block block = world.getBlockState(blockpos2).getBlock();

                double d3 = this.random.nextDouble();
                double d4 = this.random.nextDouble();

                if (block.getMaterial() == Material.lava) {
                    this.mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)blockpos1.getX() + d3, (double)((float)blockpos1.getY() + 0.1F) - block.getBlockBoundsMinY(), (double)blockpos1.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                }
                else if (block.getMaterial() != Material.air) {
                    block.setBlockBoundsBasedOnState(world, blockpos2);
                    ++j;

                    if (this.random.nextInt(j) == 0) {
                        d0 = (double)blockpos2.getX() + d3;
                        d1 = (double)((float)blockpos2.getY() + 0.1F) + block.getBlockBoundsMaxY() - 1.0D;
                        d2 = (double)blockpos2.getZ() + d4;
                    }

                    this.mc.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, (double)blockpos2.getX() + d3, (double)((float)blockpos2.getY() + 0.1F) + block.getBlockBoundsMaxY(), (double)blockpos2.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }

            if (j > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
                this.rainSoundCounter = 0;

                if (d1 > (double)(blockpos.getY() + 1) && world.getPrecipitationHeight(blockpos).getY() > MathHelper.floor_float((float)blockpos.getY())) {
                    this.mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.1F, 0.5F, false);
                } else {
                    this.mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.2F, 1.0F, false);
                }
            }
        }
    }

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
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        int l = MathHelper.floor_double(d1);
        int i1 = 5;

        if (this.mc.gameSettings.fancyGraphics) {
            i1 = 10;
        }

        int j1 = -1;
        float f1 = (float)this.rendererUpdateCount + partialTicks;
        worldrenderer.setTranslation(-d0, -d1, -d2);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int k1 = k - i1; k1 <= k + i1; ++k1) {
            for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                double d3 = (double)this.rainXCoords[i2] * 0.5D;
                double d4 = (double)this.rainYCoords[i2] * 0.5D;
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
                    this.random.setSeed((long)(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761));
                    blockpos$mutableblockpos.set(l1, k2, k1);

                    if (j1 != 1) {

                        j1 = 1;
                        this.mc.getTextureManager().bindTexture(locationRainPng);
                        worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                    }

                    double d5 = ((double)(this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + (double)partialTicks) / 32.0D * (3.0D + this.random.nextDouble());
                    double d6 = (double)((float)l1 + 0.5F) - entity.posX;
                    double d7 = (double)((float)k1 + 0.5F) - entity.posZ;
                    float f3 = MathHelper.sqrt_double(d6 * d6 + d7 * d7) / (float)i1;
                    float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * WeatherChanger.getInstance().getConfig().getStrength();
                    blockpos$mutableblockpos.set(l1, i3, k1);
                    int j3 = world.getCombinedLight(blockpos$mutableblockpos, 0);
                    int k3 = j3 >> 16 & 65535;
                    int l3 = j3 & 65535;
                    worldrenderer.pos((double)l1 - d3 + 0.5D, (double)k2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                    worldrenderer.pos((double)l1 + d3 + 0.5D, (double)k2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                    worldrenderer.pos((double)l1 + d3 + 0.5D, (double)l2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
                    worldrenderer.pos((double)l1 - d3 + 0.5D, (double)l2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
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

    @Override
    public void update() {
        this.rendererUpdateCount++;
        this.addRainParticles();
    }
}