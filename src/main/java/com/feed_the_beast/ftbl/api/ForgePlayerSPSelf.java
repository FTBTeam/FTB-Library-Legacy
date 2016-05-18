package com.feed_the_beast.ftbl.api;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 09.02.2016.
 */
@SideOnly(Side.CLIENT)
public class ForgePlayerSPSelf extends ForgePlayerSP
{
    public ForgePlayerSPSelf(GameProfile p)
    {
        super(p);
    }
    
    @Override
    public boolean isClientPlayer()
    { return true; }
    
    @Override
    public boolean isOnline()
    { return true; }
    
    @Override
    public EntityPlayer getPlayer()
    { return Minecraft.getMinecraft().thePlayer; }
    
    @Override
    public ForgePlayerSPSelf toPlayerSPSelf()
    { return this; }
    
	/*public Rank getRank()
	{
		if(rank == null) rank = new Rank("Client");
		return rank;
	}*/
    
    @Override
    public void readFromNet(NBTTagCompound tag, boolean self) // LMPlayerServer
    {
        super.readFromNet(tag, self);
    }
}
