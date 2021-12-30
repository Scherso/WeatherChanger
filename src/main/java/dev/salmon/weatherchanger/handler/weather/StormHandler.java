package dev.salmon.weatherchanger.handler.weather;

import dev.salmon.weatherchanger.WeatherChanger;
import dev.salmon.weatherchanger.config.WeatherType;
import dev.salmon.weatherchanger.util.CustomEntityLightningBolt;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public class StormHandler extends RainHandler {

    private int updateLCG = random.nextInt();
    private Field loadedChunks;

    public StormHandler(WeatherChanger weatherChanger) {
        super(weatherChanger);

        try {
            this.loadedChunks = World.class.getDeclaredField("activeChunkSet");
            this.loadedChunks.setAccessible(true);
        } catch (NoSuchFieldException e) {
            LogManager.getLogger("WeatherChanger (StormHandler)").error("Failed to find field activeChunkSet.", e);
        }
    }

    public void update() {
        super.update();
        Set<ChunkCoordIntPair> activeChunkSet;

        /* Lightning Logic */
        try {
            activeChunkSet = (Set<ChunkCoordIntPair>) loadedChunks.get(mc.theWorld);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            return;
        }

        for (ChunkCoordIntPair chunkCoord : activeChunkSet) {
            int chunkX = chunkCoord.chunkXPos * 16;
            int chunkZ = chunkCoord.chunkZPos * 16;

            int lightningChance = 100000;
            if (random.nextInt(lightningChance) == 0) {
                updateLCG = updateLCG * 3 + 1013904223;
                int offset = updateLCG >> 2;
                BlockPos lightningPos = adjustPosToNearbyEntity(new BlockPos(chunkX + (offset & 15), 0, chunkZ + (offset >> 8 & 15)));
                Minecraft.getMinecraft().theWorld.addWeatherEffect(new CustomEntityLightningBolt(mc.theWorld, lightningPos.getX(), lightningPos.getY(), lightningPos.getZ()));
            }
        }
    }

    protected BlockPos adjustPosToNearbyEntity(BlockPos pos) {
        BlockPos blockpos = mc.theWorld.getPrecipitationHeight(pos);
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockpos, new BlockPos(blockpos.getX(), mc.theWorld.getHeight(), blockpos.getZ()))).expand(3D, 3D, 3D);
        List<EntityLivingBase> list = mc.theWorld.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, p_apply_1_ -> p_apply_1_ != null && p_apply_1_.isEntityAlive() && mc.theWorld.canSeeSky(p_apply_1_.getPosition()));
        return !list.isEmpty() ? list.get(random.nextInt(list.size())).getPosition() : blockpos;
    }

    public WeatherType getType() {
        return WeatherType.STORM;
    }
}