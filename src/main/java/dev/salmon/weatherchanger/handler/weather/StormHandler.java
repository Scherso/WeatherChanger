package dev.salmon.weatherchanger.handler.weather;

import com.google.common.base.Predicate;
import dev.salmon.weatherchanger.util.CustomEntityLightningBolt;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public class StormHandler extends RainHandler {
    /* How likely it is to spawn lightning */
    private int lightningChance = 100000;
    /* Linear Congruential Generator seed for random block updates in a 16x128x16 field */
    private int updateLCG = this.random.nextInt();
    /* Reflected field from World to get loaded chunks */
    private Field loadedChunks;

    public StormHandler() {
        super(weatherChanger, true);
        try {
            this.loadedChunks = World.class.getDeclaredField("activeChunkSet");
            this.loadedChunks.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            System.out.println("Failed to find field activeChunkSet");
            ex.printStackTrace();
        }
    }

    @Override
    public void update() {
        super.update();
        Set<ChunkCoordIntPair> activeChunkSet = null;

        /* Lightning Logic */
        try {
            activeChunkSet = (Set<ChunkCoordIntPair>)this.loadedChunks.get(this.mc.theWorld);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            return;
        }

        for (ChunkCoordIntPair chunkCoord : activeChunkSet) {
            int chunkX = chunkCoord.chunkXPos * 16;
            int chunkZ = chunkCoord.chunkZPos * 16;

            if (this.random.nextInt(this.lightningChance) == 0) {

                this.updateLCG = this.updateLCG * 3 + 1013904223;
                int offset = this.updateLCG >> 2;
                BlockPos lightningPos = this.adjustPosToNearbyEntity(new BlockPos(chunkX + (offset & 15), 0, chunkZ + (offset >> 8 & 15)));

                Minecraft.getMinecraft().theWorld.addWeatherEffect(new CustomEntityLightningBolt(this.mc.theWorld, lightningPos.getX(), lightningPos.getY(), lightningPos.getZ()));
            }
        }
    }

    protected BlockPos adjustPosToNearbyEntity(BlockPos pos) {
        BlockPos blockpos = this.mc.theWorld.getPrecipitationHeight(pos);
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockpos, new BlockPos(blockpos.getX(), this.mc.theWorld.getHeight(), blockpos.getZ()))).expand(3.0D, 3.0D, 3.0D);
        List<EntityLivingBase> list = this.mc.theWorld.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, new Predicate<EntityLivingBase>() {
            public boolean apply(EntityLivingBase p_apply_1_) {
                return p_apply_1_ != null && p_apply_1_.isEntityAlive() && StormHandler.this.mc.theWorld.canSeeSky(p_apply_1_.getPosition());
            }
        });
        return !list.isEmpty() ? ((EntityLivingBase)list.get(this.random.nextInt(list.size()))).getPosition() : blockpos;
    }
}
