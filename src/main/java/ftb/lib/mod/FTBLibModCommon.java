package ftb.lib.mod;

import java.util.UUID;

import net.minecraft.entity.player.*;
import net.minecraft.world.World;

public class FTBLibModCommon // FTBLibModClient
{
	public void preInit()
	{
	}
	
	public boolean isShiftDown() { return false; }
	public boolean isCtrlDown() { return false; }
	public boolean isTabDown() { return false; }
	public boolean inGameHasFocus() { return false; }
	
	public EntityPlayer getClientPlayer() { return null; }
	public EntityPlayer getClientPlayer(UUID id) { return null; }
	public World getClientWorld() { return null; }

	public double getReachDist(EntityPlayer ep)
	{
		if(ep instanceof EntityPlayerMP)
			return ((EntityPlayerMP)ep).theItemInWorldManager.getBlockReachDistance();
		return 0D;
	}
	
	public void spawnDust(World worldObj, double x, double y, double z, int i)
	{
	}
}