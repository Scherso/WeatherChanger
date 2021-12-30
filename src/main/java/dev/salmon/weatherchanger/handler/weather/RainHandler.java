package dev.salmon.weatherchanger.handler.weather;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.config.WeatherType;
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
import org.lwjgl.opengl.GL11;

public class RainHandler extends WeatherHandler {
    private final WeatherChanger weatherChanger;

    private final ResourceLocation locationRainPng = new ResourceLocation("textures/environment/rain.png");
    private final float[] rainX = new float[1024];
    private final float[] rainY = new float[1024];
    private int rainSoundCounter;

    public RainHandler(WeatherChanger weatherChanger) {
        this.weatherChanger = weatherChanger;
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f = (float)(j - 16);
                float f1 = (float)(i - 16);
                float f2 = MathHelper.sqrt_float(f * f + f1 * f1);
                this.rainX[i << 5 | j] = -f1 / f2;
                this.rainY[i << 5 | j] = f / f2;
            }
        }
    }

    private void addRainParticles() {
        float f = weatherChanger.getConfig().getStrength();
        if (!mc.gameSettings.fancyGraphics) f /= 2f;
        if (f != 0f) {
            this.random.setSeed((long) rendererUpdateCount * 312987231L);
            Entity entity = mc.getRenderViewEntity();
            World world = mc.theWorld;
            BlockPos blockpos = new BlockPos(entity);
            int i = 10;
            double d0 = 0;
            double d1 = 0;
            double d2 = 0;
            int j = 0;
            int k = (int)(100f * f * f);

            if (this.mc.gameSettings.particleSetting == 1) {
                k >>= 1;
            } else if (this.mc.gameSettings.particleSetting == 2) {
                k = 0;
            }

            for (int l = 0; l < k; ++l) {
                BlockPos pos1 = world.getPrecipitationHeight(blockpos.add(random.nextInt(i) - random.nextInt(i), 0, random.nextInt(i) - random.nextInt(i)));
                BlockPos pos2 = pos1.down();
                Block block = world.getBlockState(pos2).getBlock();

                double d3 = random.nextDouble();
                double d4 = random.nextDouble();

                if (block.getMaterial() == Material.lava) mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) pos1.getX() + d3, (double) ((float) pos1.getY() + 0.1f) - block.getBlockBoundsMinY(), (double) pos1.getZ() + d4, 0, 0, 0);
                else if (block.getMaterial() != Material.air) {
                    block.setBlockBoundsBasedOnState(world, pos2);
                    ++j;

                    if (random.nextInt(j) == 0) {
                        d0 = (double) pos2.getX() + d3;
                        d1 = (double) ((float) pos2.getY() + 0.1f) + block.getBlockBoundsMaxY() - 1;
                        d2 = (double) pos2.getZ() + d4;
                    }

                    mc.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, (double) pos2.getX() + d3, (double) ((float) pos2.getY() + 0.1f) + block.getBlockBoundsMaxY(), (double) pos2.getZ() + d4, 0, 0, 0);
                }
            }

            if (j > 0 && random.nextInt(3) < rainSoundCounter++) {
                rainSoundCounter = 0;
                if (d1 > (double) (blockpos.getY() + 1) && world.getPrecipitationHeight(blockpos).getY() > MathHelper.floor_float((float) blockpos.getY())) mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.1f, 0.5f, false);
                else mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.2f, 1f, false);
            }
        }
    }

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
        GL11.glNormal3f(0f, 1f, 0f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.alphaFunc(516, 0.1f);
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
        int l = MathHelper.floor_double(d1);
        int i1 = 5;

        if (mc.gameSettings.fancyGraphics) i1 = 10;

        int j1 = -1;
        worldrenderer.setTranslation(-d0, -d1, -d2);
        GlStateManager.color(1f, 1f, 1f, 1f);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int k1 = k - i1; k1 <= k + i1; ++k1) {
            for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                double d3 = (double) rainX[i2] * 0.5D;
                double d4 = (double) rainY[i2] * 0.5D;
                pos.set(l1, 0, k1);

                int j2 = world.getPrecipitationHeight(pos).getY();
                int k2 = j - i1;
                int l2 = j + i1;
                if (k2 < j2) k2 = j2;
                if (l2 < j2) l2 = j2;
                int i3 = j2;
                if (j2 < l) i3 = l;
                if (k2 != l2) {
                    this.random.setSeed((long) l1 * l1 * 3121 + l1 * 45238971L ^ (long) k1 * k1 * 418711 + k1 * 13761L);
                    pos.set(l1, k2, k1);
                    if (j1 != 1) {
                        j1 = 1;
                        mc.getTextureManager().bindTexture(locationRainPng);
                        worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                    }

                    double d5 = ((double) (rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + (double) partialTicks) / 32D * (3D + random.nextDouble());
                    double d6 = (double) ((float) l1 + 0.5f) - entity.posX;
                    double d7 = (double) ((float) k1 + 0.5f) - entity.posZ;
                    float f3 = MathHelper.sqrt_double(d6 * d6 + d7 * d7) / (float)i1;
                    float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * WeatherChanger.getInstance().getConfig().getStrength();
                    pos.set(l1, i3, k1);
                    int j3 = world.getCombinedLight(pos, 0);
                    int k3 = j3 >> 16 & 65535;
                    int l3 = j3 & 65535;
                    worldrenderer.pos((double) l1 - d3 + 0.5D, k2, (double) k1 - d4 + 0.5D).tex(0D, (double) k2 * 0.25D + d5).color(1f, 1f, 1f, f4).lightmap(k3, l3).endVertex();
                    worldrenderer.pos((double) l1 + d3 + 0.5D, k2, (double) k1 + d4 + 0.5D).tex(1D, (double) k2 * 0.25D + d5).color(1f, 1f, 1f, f4).lightmap(k3, l3).endVertex();
                    worldrenderer.pos((double) l1 + d3 + 0.5D, l2, (double) k1 + d4 + 0.5D).tex(1D, (double) l2 * 0.25D + d5).color(1f, 1f, 1f, f4).lightmap(k3, l3).endVertex();
                    worldrenderer.pos((double) l1 - d3 + 0.5D, l2, (double) k1 - d4 + 0.5D).tex(0D, (double) l2 * 0.25D + d5).color(1f, 1f, 1f, f4).lightmap(k3, l3).endVertex();
                }
            }
        }

        if (j1 >= 0) tessellator.draw();
        worldrenderer.setTranslation(0D, 0D, 0D);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1f);
        renderer.disableLightmap();
    }

    public void update() {
        rendererUpdateCount++;
        addRainParticles();
    }

    public WeatherType getType() {
        return WeatherType.RAIN;
    }
}