package ftb.lib.mod;

import ftb.lib.ClientCode;
import ftb.lib.api.friends.*;
import ftb.lib.api.tile.IGuiTile;
import net.minecraft.entity.player.*;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class FTBLibModCommon // FTBLibModClient
{
	public void preInit()
	{
	}
	
	public void postInit()
	{
	}
	
	public String translate(String key, Object... obj)
	{
		if(obj == null || obj.length == 0) return StatCollector.translateToLocal(key);
		return StatCollector.translateToLocalFormatted(key, obj);
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
		if(ep instanceof EntityPlayerMP) return ((EntityPlayerMP) ep).theItemInWorldManager.getBlockReachDistance();
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
	
	public void addItemModel(String mod, Item i, int meta, String id)
	{
	}
	
	public LMWorld getForgeWorld(Side side)
	{
		if(side == null)
		{
			return getForgeWorld(FMLCommonHandler.instance().getEffectiveSide());
		}
		
		if(side.isServer())
		{
			return LMWorldMP.inst;
		}
		
		return null;
	}
	
	public void runClientCode(ClientCode c)
	{
	}
}