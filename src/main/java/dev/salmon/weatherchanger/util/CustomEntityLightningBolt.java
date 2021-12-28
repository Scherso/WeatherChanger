package dev.salmon.weatherchanger.util;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.World;

/* Gets rid of Lightning Bolt world logic, purely for Render purposes */
public class CustomEntityLightningBolt extends EntityLightningBolt {
    private int lightningState;
    private int boltLivingTime;

    public CustomEntityLightningBolt(World worldIn, double posX, double posY, double posZ) {
        super(worldIn, posX, posY, posZ);
        this.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
    }

    @Override
    public void onUpdate() {
        super.onEntityUpdate();

        if (this.lightningState == 2) {
            this.worldObj.playSound(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F, false);
            this.worldObj.playSound(this.posX, this.posY, this.posZ, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F, false);
        }

        --this.lightningState;

        if (this.lightningState < 0) {
            if (this.boltLivingTime == 0) {
                this.setDead();
            } else if (this.lightningState < -this.rand.nextInt(10)) {
                --this.boltLivingTime;
                this.lightningState = 1;
                this.boltVertex = this.rand.nextLong();
            }
        }

        if (this.lightningState >= 0) {
            if (this.worldObj.isRemote) {
                /* Affects sky color */
                this.worldObj.setLastLightningBolt(2);
            }
        }
    }
}
