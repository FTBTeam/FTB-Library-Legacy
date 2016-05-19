package com.feed_the_beast.ftbl.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public final class ForgePlayerTemp extends ForgePlayer
{
    public final Side side;
    public final EntityPlayer player;

    public ForgePlayerTemp(EntityPlayer ep)
    {
        super(ep.getGameProfile());
        side = (ep instanceof EntityPlayerMP) ? Side.SERVER : Side.CLIENT;
        player = ep;
    }

    @Override
    public Side getSide()
    {
        return side;
    }

    @Override
    public boolean isOnline()
    {
        return player != null;
    }

    @Override
    public EntityPlayer getPlayer()
    {
        return player;
    }

    @Override
    public ForgePlayerMP toPlayerMP()
    {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ForgePlayerSP toPlayerSP()
    {
        return null;
    }

    @Override
    public ForgeWorld getWorld()
    {
        return ForgeWorld.getFrom(side);
    }
}