package ftb.lib.mod;

import net.minecraft.entity.player.*;

public class FTBLibModCommon // FTBLibModClient
{
	public void preInit()
	{
	}
	
	public EntityPlayer getClientPlayer()
	{ return null; }

	public double getReachDist(EntityPlayer ep)
	{
		if(ep instanceof EntityPlayerMP)
			return ((EntityPlayerMP)ep).theItemInWorldManager.getBlockReachDistance();
		return 0;
	}
}