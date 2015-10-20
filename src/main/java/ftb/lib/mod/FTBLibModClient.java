package ftb.lib.mod;

import cpw.mods.fml.relauncher.*;
import ftb.lib.JsonHelper;
import ftb.lib.client.FTBLibClient;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public class FTBLibModClient extends FTBLibModCommon
{
	public void preInit()
	{
		JsonHelper.initClient();
	}
	
	public EntityPlayer getClientPlayer()
	{ return FTBLibClient.mc.thePlayer; }
	
	public double getReachDist(EntityPlayer ep)
	{
		if(ep == FTBLibClient.mc.thePlayer)
			return FTBLibClient.mc.playerController.getBlockReachDistance();
		else return super.getReachDist(ep);
	}
}