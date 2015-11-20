package ftb.lib.mod;

import ftb.lib.api.*;
import net.minecraft.entity.player.EntityPlayer;

public abstract class FTBUIntegration
{
	public static FTBUIntegration instance = null;
	
	public abstract void onReloaded(EventFTBReload e);
	public abstract void onModeSet(EventFTBModeSet e);
	public abstract void onFTBWorldServer(EventFTBWorldServer e);
	public abstract void onFTBWorldClient(EventFTBWorldClient e);
	public abstract void onServerTick();
	public abstract void onPlayerJoined(EntityPlayer player);
}