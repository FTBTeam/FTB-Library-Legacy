package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.ForgeWorld;
import com.feed_the_beast.ftbl.api.item.IItemLM;
import com.feed_the_beast.ftbl.api.tile.IGuiTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.UUID;

public class FTBLibModCommon // FTBLibModClient
{
	public void preInit()
	{
	}
	
	public void postInit()
	{
	}
	
	public boolean isShiftDown()
	{ return false; }
	
	public boolean isCtrlDown()
	{ return false; }
	
	public boolean isTabDown()
	{ return false; }
	
	public boolean inGameHasFocus()
	{ return false; }
	
	public EntityPlayer getClientPlayer()
	{ return null; }
	
	public EntityPlayer getClientPlayer(UUID id)
	{ return null; }
	
	public World getClientWorld()
	{ return null; }
	
	public double getReachDist(EntityPlayer ep)
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
	
	public boolean openClientGui(EntityPlayer ep, String mod, int ID, NBTTagCompound data)
	{
		return false;
	}
	
	public void openClientTileGui(EntityPlayer ep, IGuiTile t, NBTTagCompound data)
	{
	}
	
	public ForgeWorld getClientLMWorld()
	{ return null; }
	
	public void loadModels(IItemLM i)
	{
	}
}