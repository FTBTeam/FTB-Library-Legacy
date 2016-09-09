package com.feed_the_beast.ftbl;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FTBLibModCommon // FTBLibModClient
{
    public void preInit()
    {
    }

    public void postInit()
    {
    }

    @Nullable
    public EntityPlayer getClientPlayer()
    {
        return null;
    }

    public double getReachDist(@Nullable EntityPlayer ep)
    {
        if(ep instanceof EntityPlayerMP)
        {
            return ((EntityPlayerMP) ep).interactionManager.getBlockReachDistance();
        }

        return 0D;
    }

    public void spawnDust(World worldObj, double x, double y, double z, int i)
    {
    }
}