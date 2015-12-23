package ftb.lib.mod;

import ftb.lib.api.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public interface FTBUIntegration // FTBLIntegration
{
	public void onReloaded(EventFTBReload e);
	public void onModeSet(EventFTBModeSet e);
	public void onFTBWorldServer(EventFTBWorldServer e);
	public void onFTBWorldClient(EventFTBWorldClient e);
	public void onServerTick(World w);
	public void onPlayerJoined(EntityPlayerMP player);
	public int getPlayerID(Object player);
	public String[] getPlayerNames(boolean online);
}