package ftb.lib.mod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.api.config.ConfigListRegistry;
import ftb.lib.mod.net.*;
import latmod.lib.FastList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;

public class FTBLibEventHandler
{
	public static final FastList<ServerTickCallback> callbacks = new FastList<ServerTickCallback>();
	public static final FastList<ServerTickCallback> pendingCallbacks = new FastList<ServerTickCallback>();
	
	@SubscribeEvent
	public void onServerStarted(WorldEvent.Load e)
	{
		if(e.world.provider.dimensionId == 0 && e.world instanceof WorldServer)
		{
			ConfigListRegistry.reloadInstance();
			FTBWorld.reloadGameModes();
			FTBWorld.server = new FTBWorld(e.world);
			new EventFTBWorldServer(FTBWorld.server).post();
			FTBLibMod.reload(FTBLib.getServer(), false);
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
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent e)
	{
		if(!e.world.isRemote && e.side == Side.SERVER && e.phase == TickEvent.Phase.END && e.type == TickEvent.Type.WORLD && e.world.provider.dimensionId == 0)
		{
			if(!pendingCallbacks.isEmpty())
			{
				callbacks.addAll(pendingCallbacks);
				pendingCallbacks.clear();
			}
			
			if(!callbacks.isEmpty())
			{
				for(int i = callbacks.size() - 1; i >= 0; i--)
					if(callbacks.get(i).incAndCheck())
						callbacks.remove(i);
			}
		}
	}
}