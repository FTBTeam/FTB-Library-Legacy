package ftb.lib.mod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import ftb.lib.FTBWorld;
import ftb.lib.api.EventFTBWorldServer;
import ftb.lib.mod.net.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;

public class FTBLibEventHandler
{
	@SubscribeEvent
	public void onServerStarted(WorldEvent.Load e)
	{
		if(e.world.provider.dimensionId == 0 && e.world instanceof WorldServer)
		{
			FTBWorld.reloadGameModes();
			FTBWorld.server = new FTBWorld(e.world);
			new EventFTBWorldServer(FTBWorld.server).post();
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent e)
	{
		if(e.player instanceof EntityPlayerMP)
		{
			EntityPlayerMP ep = (EntityPlayerMP)e.player;
			new MessageSyncConfig(ep).sendTo(ep);
			new MessageSendWorldID(FTBWorld.server).sendTo(ep);
			new MessageSendGameMode(FTBWorld.server.getMode()).sendTo(ep);
		}
	}
}